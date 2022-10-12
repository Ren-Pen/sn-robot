package com.slimenano.nscan.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.slimenano.framework.RobotApplication;
import com.slimenano.framework.commons.ClassUtils;
import com.slimenano.framework.commons.PackageScanner;
import com.slimenano.sdk.config.ConfigFields;
import com.slimenano.sdk.config.ConfigLocation;
import com.slimenano.sdk.config.Configuration;
import com.slimenano.sdk.config.DefaultConfiguration;
import com.slimenano.sdk.framework.Context;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.annotations.Collections;
import com.slimenano.sdk.framework.annotations.*;
import com.slimenano.sdk.framework.exception.BeanException;
import com.slimenano.sdk.framework.exception.BeanInitializationException;
import com.slimenano.sdk.framework.exception.BeanRepeatNameException;
import com.slimenano.sdk.framework.exception.GetBeanException;
import com.slimenano.sdk.logger.Marker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.slimenano.framework.commons.JSONUtils.om;

/**
 * Bean管理器
 */
@Slf4j
@Marker("对象上下文")
public class BeanContext implements Context {

    // 所属Map，当一个bean被初始化后，他将会永远属于初始化他的context。
    private static final Map<Class<?>, Class<?>> belongMap = new LinkedHashMap<>();
    private static final Map<Class<?>, BeanContext> contextMap = new LinkedHashMap<>();
    private static final String dataDirectory = "data/";

    static {
        new File(dataDirectory).mkdirs();
    }

    @Getter
    private final Class<?> contextClass;
    private final File configFile;
    private final String scanPackage;

    @Getter
    private final Map<String, Object> beanNameMap = new LinkedHashMap<>();

    private volatile int status = CREATED;
    private ClassLoader beanClassLoader = this.getClass().getClassLoader();

    private HashMap<String, Object> configuration;

    private BeanContext(Class<?> createClass) throws IOException {
        log.debug("{} 准备构造对象上下文.", createClass.getName());
        this.contextClass = createClass;
        ScanPackageLocation scanPackageLocation = contextClass.getAnnotation(ScanPackageLocation.class);
        if (scanPackageLocation != null) {
            scanPackage = scanPackageLocation.value();
        } else {
            scanPackage = contextClass.getPackage().getName();
        }

        ConfigLocation configLocation = createClass.getAnnotation(ConfigLocation.class);

        if (configLocation != null) {
            String d = dataDirectory + createClass.getName() + "/";
            if (ClassUtils.hasOrgAnnotation(createClass, ISystem.class)) {
                d = dataDirectory;
            }
            File dir = new File(d);
            File file = new File(d + configLocation.location() + ".json");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                om.writerWithDefaultPrettyPrinter().writeValue(file, "{}");
            }
            configFile = file;
            configuration = om.readValue(file, HashMap.class);
            log.debug("{} 加载上下文配置文件: {}, 大小:{}字节", createClass.getName(), file.getName(), file.length());
        } else {
            configFile = null;
            configuration = null;
        }
    }

    public static BeanContext createContext(Class<?> clazz) throws Exception {
        return createContext(clazz, null, true);
    }

    public static BeanContext createContext(Class<?> clazz, ClassLoader classLoader, boolean autowired) throws Exception {
        long time = System.currentTimeMillis();
        BeanContext context = contextMap.getOrDefault(clazz, new BeanContext(clazz));
        contextMap.put(clazz, context);
        log.debug("{} 开始扫描包并初始化对象，包路径：{}", context.contextClass, context.scanPackage);
        if (classLoader != null)
            context.beanClassLoader = classLoader;
        context.scanAndInitializeBean();
        log.debug("{} 开始自动装填对象上下文", context.contextClass);
        if (autowired)
            context.refreshAutowiredBean();
        log.debug("{} 对象上下文初始化完成，耗时：{}ms", context.contextClass, System.currentTimeMillis() - time);
        return context;
    }

    public static void removeContext(Class<?> clazz) {
        if (contextMap.containsKey(clazz)) {
            BeanContext context = contextMap.get(clazz);
            removeContext(context);
        }
    }

    /**
     * 卸载逻辑更改，清空所有的属性，并查找所有的thread属性并关闭
     *
     * @param context
     */
    public static void removeContext(BeanContext context) {
        log.debug("{} 准备卸载对象上下文", context.contextClass);

        List<Object> beans = context.getBeans();
        for (Object o : beans) {
            if (context.isBelongThisContext(o.getClass())) {
                if (o instanceof InitializationBean) {
                    try {
                        ((InitializationBean) o).onDestroy();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if (context.contextClass != RobotApplication.class) {
                    for (Field f : ClassUtils.getAllField(o.getClass())) {
                        f.setAccessible(true);
                        Class<?> type = f.getType();
                        if (Thread.class.isAssignableFrom(type)) {
                            try {
                                Thread thd = (Thread) f.get(o);
                                if (thd.isAlive()) {
                                    log.warn("{}.{}<{}> 发现未停止的线程，正在尝试停止", o.getClass(), f, thd.getName());
                                    thd.interrupt();
                                }
                            } catch (IllegalAccessException ignore) {

                            }
                        } else if (ThreadPoolExecutor.class.isAssignableFrom(type)) {

                            try {
                                ThreadPoolExecutor thd = (ThreadPoolExecutor) f.get(o);
                                if (!thd.isTerminated()) {
                                    log.warn("{}.{}<{}> 发现未停止的线程池子，正在尝试停止", o.getClass(), f, type);
                                    thd.shutdownNow();
                                }
                            } catch (IllegalAccessException ignore) {

                            }

                        }
                        try {
                            int modifiers = type.getModifiers();
                            if (!type.isPrimitive() && !Modifier.isFinal(modifiers))
                                f.set(o, null);
                            if (!type.isPrimitive() && !String.class.isAssignableFrom(type) && Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers)) {
                                log.warn("{}.{}<{}> 发现静态常量，可能会导致内存泄漏", o.getClass(), f, type);
                            }
                        } catch (IllegalAccessException ignore) {

                        }
                    }
                }
                belongMap.remove(o.getClass());
            }
        }

        // 清除掉 jackson 的缓存，因为会导致插件类卸载不全
        TypeFactory.defaultInstance().clearCache();
        om = new ObjectMapper();
        context.beanNameMap.clear();
        contextMap.remove(context.contextClass);
        log.debug("{} 对象上下文已卸载，已通知GC", context.contextClass);
        System.gc();

    }

    public static BeanContext getContext(Class<?> clazz) {
        return contextMap.get(clazz);
    }

    public boolean isBelongThisContext(Class<?> clazz) {
        return this.contextClass.equals(belongMap.get(clazz));
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void close() {
        this.status = BEFORE_DESTROY;
        removeContext(this);
        this.status = DESTROYED;
    }

    private void scanAndInitializeBean() throws Exception {
        Set<Class<?>> set = new LinkedHashSet<>();
        long time = System.currentTimeMillis();
        createScanner(set).scanPackage(scanPackage, beanClassLoader);
        log.debug("{} 包扫描耗时：{}ms", contextClass, System.currentTimeMillis() - time);
        this.status = SCANNED;
        ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(beanClassLoader);
            time = System.currentTimeMillis();
            initializeBean(set);
            log.debug("{} 初始化耗时：{}ms", contextClass, System.currentTimeMillis() - time);
        } finally {
            Thread.currentThread().setContextClassLoader(appClassLoader);
        }
        this.status = INITIALIZED;
        storeConfiguration();
    }

    private PackageScanner createScanner(Set<Class<?>> set) {
        return new PackageScanner() {
            @Override
            public boolean dealClass(Class<?> clazz) {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    return true;
                }

                if (ClassUtils.hasOrgAnnotation(clazz, Belong.class)) {
                    if (!belongMap.containsKey(clazz)) belongMap.put(clazz, BeanContext.this.contextClass);
                }

                if (ClassUtils.hasOrgAnnotation(clazz, Instance.class)) {
                    log.debug("{} 加载到被管理对象: {}", BeanContext.this.contextClass, clazz);
                    set.add(clazz);
                }

                return true;
            }
        };
    }

    private void initializeBean(Set<Class<?>> set) throws BeanInitializationException {
        for (Class<?> clazz : set) {
            Object o = null;
            try {
                Configuration config = clazz.getAnnotation(Configuration.class);
                FromInstance fromInstance = clazz.getAnnotation(FromInstance.class);
                String cfx;
                if (configuration != null && config != null) {
                    ConfigFields configFields = clazz.getAnnotation(ConfigFields.class);
                    log.debug("{} 构建并初始化配置对象: {}", this.contextClass, clazz);
                    if (!configuration.containsKey(config.prefix())) {
                        if (DefaultConfiguration.class.isAssignableFrom(clazz)) {
                            log.warn("配置项 | 配置 {} 丢失, 即将构建默认配置", config.prefix());
                            o = clazz.newInstance();
                        } else {
                            throw new BeanInitializationException(String.format("初始化被管理对象失败! 配置 %s 不存在", config.prefix()));
                        }
                    } else {
                        if (configFields != null) {
                            List<Object> list = new ArrayList<>(configFields.fieldName().length);
                            for (int i = 0; i < configFields.fieldName().length; i++) {
                                try {
                                    String s = configFields.fieldName()[i];
                                    Class<?> cf = configFields.fieldType()[i];
                                    list.add(om.convertValue(((HashMap) configuration.get(config.prefix())).get(s), cf));
                                } catch (Exception e) {
                                    throw new BeanInitializationException(String.format("初始化被管理对象失败！设置了配置字段但字段数据错误！配置：%s", config.prefix()), e);
                                }
                            }
                            try {
                                o = clazz.getConstructor(configFields.fieldType()).newInstance(list.toArray());
                            } catch (Exception e) {
                                throw new BeanInitializationException(String.format("初始化被管理对象失败！配置 %s 无法创建实例！", config.prefix()), e);
                            }
                        } else {
                            o = om.convertValue(configuration.get(config.prefix()), clazz);
                        }
                    }
                    cfx = "config:" + config.prefix();
                } else {

                    if (fromInstance != null) {
                        log.debug("{} 非容器构建的被管理对象: {}", this.contextClass, clazz);
                        Field field = clazz.getDeclaredField(fromInstance.field());
                        field.setAccessible(true);
                        if (!Modifier.isStatic(field.getModifiers())) {
                            throw new IllegalArgumentException("字段非静态！");
                        }
                        o = field.get(null);
                        if (o == null) throw new NullPointerException("字段值为空！");
                    } else {
                        log.debug("{} 构建并初始化被管理对象: {}", this.contextClass, clazz);
                        o = clazz.newInstance();
                    }
                    cfx = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                }

                if (beanNameMap.containsKey(cfx)) {
                    cfx = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                    if (beanNameMap.containsKey(cfx)) {
                        while (beanNameMap.containsKey(cfx)) {
                            cfx = String.valueOf(UUID.randomUUID());
                        }
                        log.debug("{} 无法找到合适的对象名称，对象将使用随机名称，对象：{}  名称：{}", this.contextClass, clazz, cfx);
                    }
                }

                // 加入别名
                InstanceAliases aliases = clazz.getAnnotation(InstanceAliases.class);
                beanNameMap.put(cfx, o);
                InstanceAlias alias = clazz.getAnnotation(InstanceAlias.class);
                if (aliases != null) {
                    for (int i = 0; i < aliases.value().length; i++) {
                        alias = aliases.value()[i];
                        if (beanNameMap.containsKey(alias.alias())) {
                            throw new BeanRepeatNameException(String.format("%s 别名已被注册 源：%s 现有：%s", alias.alias(), clazz, beanNameMap.get(alias.alias()).getClass()));
                        } else {
                            beanNameMap.put(alias.alias(), o);
                        }

                    }
                } else if (alias != null) {
                    if (beanNameMap.containsKey(alias.alias())) {
                        throw new BeanRepeatNameException(String.format("%s 别名已被注册 源：%s 现有：%s", alias.alias(), clazz, beanNameMap.get(alias.alias()).getClass()));
                    } else {
                        beanNameMap.put(alias.alias(), o);
                    }
                }


            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | BeanException e) {
                if (o != null) {
                    Object finalO = o;
                    String[] names = beanNameMap.keySet().stream().filter(s -> beanNameMap.get(s) == finalO).toArray(String[]::new);
                    for (String name : names) {
                        beanNameMap.remove(name);
                    }
                }
                throw new BeanInitializationException("初始化被管理对象失败！", e);
            }
        }
    }

    public void refreshAutowiredBean() throws BeanException {
        if (status > LOADED) throw new BeanException("DEAD CONTEXT");
        for (Object o : getBeans()) {

            Class<?> clazz = o.getClass();

            if (!isBelongThisContext(clazz)) {
                continue;
            }

            log.debug("{} 准备自动装载 {}", this.contextClass, clazz);

            for (Field field : ClassUtils.getAllField(clazz)) {
                field.setAccessible(true);
                Mount mount = field.getAnnotation(Mount.class);
                Collections collections = field.getAnnotation(Collections.class);
                try {
                    if (mount != null && collections != null)
                        throw new BeanInitializationException("不允许同时被 Autowired 和 Collections 装载");
                    Class<?> fieldType = field.getType();
                    if (mount != null) {

                        if (field.get(o) != null) {
                            continue;
                        }

                        Class<?> type = fieldType;
                        // 获取泛型字段的真实类型
                        GenericClassField genericClassField = field.getAnnotation(GenericClassField.class);
                        if (genericClassField != null) {
                            type = (Class<?>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[genericClassField.slot()];
                        }
                        if (type.isAssignableFrom(this.getClass())) {
                            field.set(o, this);
                        } else {
                            // 如果autowired上有名称，则直接使用名称
                            // 如果没有则先使用默认名称，再使用类型注入

                            if (mount.value().isEmpty()) {
                                String cfx = field.getName();
                                try {
                                    field.set(o, getBean(cfx));
                                } catch (GetBeanException e) {
                                    field.set(o, getBean(type));
                                }
                            } else {
                                field.set(o, getBean(mount.value()));
                            }

                        }
                        log.debug("自动装载 | 已装载 {} 属性 {}", clazz, field);


                    } else if (collections != null) {
                        if (field.get(o) != null) {
                            continue;
                        }
                        Class<?> type = fieldType;
                        if (type.isArray()) {
                            type = type.getComponentType();
                        } else if (Collection.class.isAssignableFrom(type)) {
                            type = (Class<?>) ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments()[0];
                        } else {
                            throw new BeanInitializationException("无法识别的类型，自动装载无法包装为Collection");
                        }
                        List<?> beans;
                        if (collections.value().isEmpty()) {
                            beans = getBeans(type);
                        } else {
                            beans = getBeans(collections.value(), type);
                        }
                        int modifiers = fieldType.getModifiers();
                        if (Collection.class.isAssignableFrom(fieldType)) {
                            if (!Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers)) {
                                field.set(o, fieldType.getConstructor(Collection.class).newInstance(beans));
                            } else {
                                if (List.class.isAssignableFrom(fieldType)) {
                                    field.set(o, beans);
                                } else if (Set.class.isAssignableFrom(fieldType)) {
                                    field.set(o, new HashSet<>(beans));
                                }
                            }
                        } else if (fieldType.isArray()) {
                            Object array = Array.newInstance(type, beans.size());
                            for (int i = 0; i < beans.size(); i++) {
                                Array.set(array, i, beans.get(i));
                            }
                            field.set(o, array);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    throw new BeanInitializationException(e);
                }
            }

        }
        this.status = WIRED;
    }

    public void notifyLoad() throws BeanInitializationException {
        ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(beanClassLoader);
            for (Object o : getBeans()) {
                if (this.isBelongThisContext(o.getClass())) {
                    if (o instanceof InitializationBean) {
                        try {
                            ((InitializationBean) o).onLoad();
                        } catch (Exception e) {
                            throw new BeanInitializationException(e);
                        }
                    }
                }
            }
        } finally {
            Thread.currentThread().setContextClassLoader(appClassLoader);
        }

        this.status = LOADED;

    }


    /**
     * 以当前的bean保存到配置文件中
     */
    @Override
    public void storeConfiguration() throws IOException {
        if (configuration == null) return;
        HashMap<String, Object> root = new HashMap<>();
        for (Object bean : this.getBeans()) {
            Class<?> clazz = bean.getClass();
            Configuration configuration = clazz.getAnnotation(Configuration.class);
            if (configuration != null) {
                ConfigFields fields = clazz.getAnnotation(ConfigFields.class);
                if (fields == null) {
                    root.put(configuration.prefix(), bean);
                } else {
                    HashMap map = new HashMap();
                    try {
                        for (String s : fields.fieldName()) {
                            Field cf = clazz.getDeclaredField(s);
                            cf.setAccessible(true);
                            map.put(s, cf.get(bean));
                        }
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                    root.put(configuration.prefix(), bean);

                }

            }
        }

        om.writerWithDefaultPrettyPrinter().writeValue(configFile, root);

    }

    @Override
    public void addBean(String name, Object o) throws BeanRepeatNameException {
        if (beanNameMap.containsKey(name)) throw new BeanRepeatNameException("对象名称重复注册");
        beanNameMap.put(name, o);
        //beanClassMap.put(o.getClass(), o);
    }

    @Override
    public <T> T getBeanOrNull(Class<T> clazz) {
        try {
            return getBean(clazz);
        } catch (GetBeanException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getBean(String name) throws GetBeanException {
        if (!beanNameMap.containsKey(name)) {
            throw new GetBeanException("没有找到符合条件的被管理对象：" + name + " 对象名未注册");
        }
        return beanNameMap.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws GetBeanException {
        Object o = getBean(name);
        if (!clazz.isAssignableFrom(o.getClass())) {
            throw new GetBeanException("没有找到符合条件的被管理对象：" + name + " 类型不匹配！");
        }
        return (T) o;
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws GetBeanException {

        String beanName = clazz.getSimpleName();
        beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

        try {
            Object bean = getBean(beanName);
            if (clazz.isAssignableFrom(bean.getClass()))
                return (T) bean;
        } catch (GetBeanException ignored) {
        }

        List<T> beans = getBeans(clazz);
        if (beans.isEmpty()) throw new GetBeanException("没有找到符合条件的被管理对象：" + clazz.getSimpleName() + " 对象未注册");
        if (beans.size() > 1) {
            throw new GetBeanException("发现多个符合条件的被管理对象，上下文无法裁决返回值");
        }

        return beans.get(0);
    }

    @Override
    public List<Object> getBeans() {
        return beanNameMap.values().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public <T> List<T> getBeans(Class<T> clazz) {

        return beanNameMap.values().stream()
                .filter(o -> clazz.isAssignableFrom(o.getClass())) // 过滤
                .map(o -> (T) o).distinct() // 转换并去重
                .collect(Collectors.toCollection(LinkedList::new)); // 转成链表
    }

    @Override
    public <T> List<T> getBeans(String regex, Class<T> clazz) {

        Pattern pattern = Pattern.compile(regex);

        return beanNameMap.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getValue().getClass()) && pattern.matcher(entry.getKey()).matches()) // 过滤
                .map(entry -> (T) entry.getValue()).distinct() // 转换并去重
                .collect(Collectors.toCollection(LinkedList::new)); // 转成链表

    }

    @Override
    public List<Object> getBeans(String regex) {
        return getBeans(regex, Object.class);
    }


}
















