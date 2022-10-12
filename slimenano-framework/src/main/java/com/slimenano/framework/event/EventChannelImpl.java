package com.slimenano.framework.event;

import com.slimenano.framework.event.impl.ISysEvent;
import com.slimenano.nscan.framework.BeanContext;
import com.slimenano.nscan.framework.ISystem;
import com.slimenano.nscan.framework.SystemInstance;
import com.slimenano.sdk.event.annotations.*;
import com.slimenano.sdk.event.annotations.EventListener;
import com.slimenano.sdk.framework.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.slimenano.framework.commons.ClassUtils;
import com.slimenano.sdk.event.EventChannel;
import com.slimenano.sdk.event.IEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.logger.Marker;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SystemInstance
@Slf4j
@Marker("事件总线")
public class EventChannelImpl implements InitializationBean, Runnable, EventChannel {

    private static final int MAX_PRIORITY = 9999;
    private static final int MIN_PRIORITY = -9999;
    private static final int HIGHEST_PRIORITY = 0;

    // 事件总线缓存目录
    private static final String dataDirectory = "cache/event/";
    private static final int REG = 0;
    private static final int D_REG = 1;
    private static final Object lock = new Object();

    // 事件总线是否停止
    private volatile static boolean stop = false;
    // 事件总线自身线程
    @Getter
    private final Thread thread = new Thread(this, "EventChannel");
    private final HashSet<BeanContext> registerContextSet = new HashSet<>();
    // Context-Listener-Map
    private final HashMap<Class<?>, LinkedList<SubscriberMethod>> beans_map = new LinkedHashMap<>();
    // Event-Listener-Map
    private final HashMap<Class<? extends IEvent>, LinkedList<SubscriberMethod>> subscribers_map = new HashMap<>();
    // 执行队列
    private final Queue<EventTask> operatorQueue = new ConcurrentLinkedQueue<>();
    // 事件队列
    private final Queue<IEvent> eventsQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<Context, Runnable> actionMap = new ConcurrentHashMap<>();
    private final Queue<Class<?>> deadListener = new ConcurrentLinkedQueue<>();

    public EventChannelImpl() {

        new File(dataDirectory).mkdirs();


    }

    public EventChannelImpl addUnregisterActionListener(Context context, Runnable runnable) {
        actionMap.put(context, runnable);
        return this;
    }

    // 优先级重排
    private void sort_list(Class<? extends IEvent> eventClazz) {
        LinkedList<SubscriberMethod> methods_list = subscribers_map.get(eventClazz);
        if (methods_list.isEmpty()) return;
        methods_list.sort((o1, o2) -> {
            if (o1.getPriority() == o2.getPriority()) return 0;
            else if (o1.getPriority() == 0) return -1;
            else if (o2.getPriority() == 0) return 1;
            else if (o1.getPriority() < 0 && o2.getPriority() > 0) return -1;
            else if (o1.getPriority() > 0 && o2.getPriority() < 0) return 1;
            else if (o1.getPriority() < 0 && o2.getPriority() < 0) {
                return o1.getPriority() > o2.getPriority() ? 1 : -1;
            }

            return o1.getPriority() < o2.getPriority() ? 1 : -1;
        });
        log.debug("{} 优先级队列已更新", eventClazz);
    }

    public void register(Class<?> clazz) {
        BeanContext context = BeanContext.getContext(clazz);
        if (context == null) return;
        operatorQueue.add(new EventTask(REG, context));
        log.debug("{} 准备注册对象上下文 位置：[{}]", clazz, operatorQueue.size());
    }

    public void register(BeanContext context) {
        operatorQueue.add(new EventTask(REG, context));
        log.debug("{} 准备注册对象上下文 位置：[{}]", context.getContextClass(), operatorQueue.size());
    }

    public void unregister(BeanContext context) {
        operatorQueue.add(new EventTask(D_REG, context));
        log.debug("准备注销对象上下文 位置：[{}]", operatorQueue.size());
    }

    public void unregister(Class<?> clazz) {
        BeanContext context = BeanContext.getContext(clazz);
        if (context == null) return;
        operatorQueue.add(new EventTask(D_REG, context));
        log.debug("准备注销对象上下文 位置：[{}]", operatorQueue.size());
    }

    private void dRegister(BeanContext context) {

        LinkedHashSet<Class<? extends IEvent>> update_set = new LinkedHashSet<>();
        Base64.Decoder decoder = Base64.getDecoder();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Object o : context.getBeans()) {

            Class<?> beansClass = o.getClass();
            if (beans_map.containsKey(beansClass)) continue;
            if (!context.isBelongThisContext(beansClass)) continue;

            if (ClassUtils.hasOrgAnnotation(beansClass, EventListener.class)) {
                long timestart = System.currentTimeMillis();
                int count = 0;
                log.debug("{} 正在注册监听器", beansClass);

                CacheIndex cache = beansClass.getAnnotation(CacheIndex.class);

                File cacheFile = new File(dataDirectory + beansClass.getName() + ".cache");
                if (cache != null) {

                    log.debug("{} 准备加载缓存：{}", beansClass, cacheFile);
                    if (!cacheFile.exists()) {
                        log.warn("{} 没有找到缓存文件，即将重新构建缓存。", beansClass);
                    } else {
                        int lineNO = 1;
                        try (BufferedReader reader = new BufferedReader(new FileReader(cacheFile))) {
                            String header = reader.readLine();
                            checkFormat(cache, header);
                            String line;
                            LinkedList<SubscriberMethod> cacheList = new LinkedList<>();

                            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                                lineNO++;
                                cacheList.add(cacheSubscribeMethod(update_set, decoder, beansClass, o, line));
                            }

                            for (SubscriberMethod value : cacheList) {
                                String flag = registerSubscriberMethod(update_set, beansClass, value);
                                count++;
                                log.debug("{} 事件方法注册完成  方法：{}  优先级：{}", beansClass, value.getMethod().getName(), flag);
                            }

                            log.debug("{} 监听器已注册完成：[{}] 耗时：{}ms", beansClass, count, System.currentTimeMillis() - timestart);
                            continue;

                        } catch (Exception e) {
                            log.warn("{} 加载缓存时出现错误，即将重新构建缓存，错误行：{}", beansClass, lineNO, e);
                        }
                    }
                }

                StringBuilder cacheBuilder = new StringBuilder();
                if (cache != null) {
                    cacheBuilder.append("Cache-Version: ").append(cache.version()).append('\n');
                }

                Method[] declaredMethods = beansClass.getDeclaredMethods();
                OnSuperSubscribe superSubscribe = ClassUtils.getOrgAnnotation(beansClass, OnSuperSubscribe.class);
                if (superSubscribe != null) {
                    Method[] fdms = superSubscribe.superClazz().getDeclaredMethods();
                    declaredMethods = Arrays.stream(beansClass.getMethods())
                            .filter(method -> {
                                method.setAccessible(false);
                                return Arrays.stream(fdms).anyMatch(fm -> {
                                    fm.setAccessible(false);
                                    return fm.getName().equals(method.getName());
                                });
                            })
                            .toArray(Method[]::new);
                }
                for (Method method : declaredMethods) {
                    method.setAccessible(false);
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    if (subscribe != null) {
                        subscribeMethod(update_set, encoder, beansClass, o, cache, cacheBuilder, method, subscribe);
                        count++;
                    }
                }
                writeCache(beansClass, cache, cacheFile, cacheBuilder);

                log.debug("{} 监听器已注册完成：[{}] 耗时：{}ms", beansClass, count, System.currentTimeMillis() - timestart);


            }


        }
        registerContextSet.add(context);
        update_set.forEach(this::sort_list);


    }

    private SubscriberMethod cacheSubscribeMethod(LinkedHashSet<Class<? extends IEvent>> update_set, Base64.Decoder decoder, Class<?> beansClass, Object o, String line) throws ClassNotFoundException, NoSuchMethodException {
        log.debug("{} 读入缓存行：{}", beansClass, line);
        String[] s = new String(decoder.decode(line), StandardCharsets.UTF_8).split(" ");
        log.debug("{} 解析行：{}", beansClass, s);
        String methodName = s[0];
        Class<?> eventType = Class.forName(s[1]);
        int priority = Integer.parseInt(s[2]);
        boolean isSys = Boolean.parseBoolean(s[3]);
        priority = verifyPriority(priority, isSys);

        return new SubscriberMethod(o, beansClass.getDeclaredMethod(methodName, eventType), (Class<? extends IEvent>) eventType, priority);
    }

    private void checkFormat(CacheIndex cache, String header) {
        if (!header.startsWith("Cache-Version: ")) {
            throw new RuntimeException("非法的格式。");
        }
        int version = Integer.parseInt(header.substring(15));
        if (cache.version() < version) {
            throw new RuntimeException("过期的版本");
        } else if (cache.version() > version) {
            throw new RuntimeException("过期的客户端");
        }
    }

    private void subscribeMethod(LinkedHashSet<Class<? extends IEvent>> update_set, Base64.Encoder encoder, Class<?> beansClass, Object o, CacheIndex cache, StringBuilder cacheBuilder, Method method, Subscribe subscribe) {
        if (method.getParameterCount() != 1 || !IEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new IllegalArgumentException("无法装载的方法类型，接收事件方法仅允许接收一个事件参数!");
        }

        int priority = subscribe.priority();
        boolean isSys = ClassUtils.hasOrgAnnotation(beansClass, ISystem.class);
        priority = verifyPriority(priority, isSys);


        Class<? extends IEvent> parameterType = (Class<? extends IEvent>) method.getParameterTypes()[0];
        GenericEventType genericEventType = method.getAnnotation(GenericEventType.class);
        if (genericEventType != null) {
            parameterType = (Class<? extends IEvent>) ClassUtils.getGenerateClass(o.getClass(), genericEventType.slot());
        }

        SubscriberMethod value = new SubscriberMethod(o, method, parameterType, priority);
        String flag = registerSubscriberMethod(update_set, beansClass, value);
        if (cache != null) {
            String line = String.format("%s %s %d %b", method.getName(), value.getEventType().getName(), value.getPriority(), isSys);
            String str = encoder.encodeToString(line.getBytes(StandardCharsets.UTF_8));
            cacheBuilder.append(str).append('\n');
            log.debug("{} 缓存行：{}", beansClass, str);
        }
        log.debug("{} 事件方法注册完成  方法：{} 接收事件：{} 优先级：{}", beansClass, value.getMethod().getName(), value.getEventType(), flag);
    }

    private void writeCache(Class<?> beansClass, CacheIndex cache, File cacheFile, StringBuilder cacheBuilder) {
        if (cache != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(cacheFile))) {
                writer.println(cacheBuilder);
                log.debug("{} 已缓存", beansClass);
            } catch (Exception e) {
                log.error("{} 缓存保存失败", beansClass, e);
            }
        }
    }

    private String registerSubscriberMethod(LinkedHashSet<Class<? extends IEvent>> update_set, Class<?> beansClass, SubscriberMethod value) {

        if (!beans_map.containsKey(beansClass)) {
            beans_map.put(beansClass, new LinkedList<>());
        }
        beans_map.get(beansClass).add(value);
        if (!subscribers_map.containsKey(value.getEventType())) {
            subscribers_map.put(value.getEventType(), new LinkedList<>());
        }
        subscribers_map.get(value.getEventType()).add(value);
        update_set.add(value.getEventType());
        String flag = String.valueOf(value.getPriority());
        if (value.getPriority() < 0) {
            flag = "[系统] " + -value.getPriority();
        } else if (value.getPriority() == 0) {
            flag = "[最高]";
        }
        return flag;
    }

    private int verifyPriority(int priority, boolean isSys) {
        if (isSys) {
            if (priority > HIGHEST_PRIORITY) priority = -priority;
            if (priority < MIN_PRIORITY) priority = MIN_PRIORITY;
        } else {
            if (priority <= HIGHEST_PRIORITY) priority = 1;
            if (priority > MAX_PRIORITY) priority = MAX_PRIORITY;
        }
        return priority;
    }

    private void dUnregister(BeanContext context) {

        LinkedHashSet<Class<? extends IEvent>> update_set = new LinkedHashSet<>();


        for (Object o : context.getBeans()) {
            Class<?> beansClass = o.getClass();
            if (!context.isBelongThisContext(beansClass)) {
                continue;
            }
            if (beans_map.containsKey(beansClass)) {
                LinkedList<SubscriberMethod> methods = beans_map.get(beansClass);
                methods.forEach((method) -> {
                    update_set.add(method.getEventType());
                    subscribers_map.get(method.getEventType()).remove(method);
                });
                beans_map.remove(beansClass);
                log.debug("{} 监听器已注销", beansClass);
            }
        }
        log.debug("{} 对象上下文已从事件总线中注销", context.getContextClass());
        registerContextSet.remove(context);
        actionMap.getOrDefault(context, () -> {
        }).run();
        actionMap.remove(context);
        update_set.forEach(this::sort_list);
    }

    /**
     * 同步事件处理
     *
     * @param event
     */
    private void dealEventSync(IEvent event) {

        Class<?> eventClass = event.getClass();
        while (IEvent.class.isAssignableFrom(eventClass)) {
            if (subscribers_map.containsKey(eventClass)) {
                LinkedList<SubscriberMethod> methods = subscribers_map.getOrDefault(eventClass, new LinkedList<>());
                for (SubscriberMethod method : methods) {
                    Class<?> oClass = method.getO().getClass();
                    if (event instanceof ISysEvent && !ClassUtils.hasOrgAnnotation(oClass, ISystem.class)) {
                        log.debug("{} 非系统对象，已跳过监听", oClass);
                        continue;
                    }

                    ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        log.debug("{} 分发至 {}.{}()", event, oClass, method.getMethod().getName());
                        Thread.currentThread().setContextClassLoader(oClass.getClassLoader());
                        method.getMethod().invoke(method.getO(), event);
                        if (event.isPrevent()) {
                            log.debug("{} 被终止", event);
                            break;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("{} 在执行监听方法 {}.{}() 时出错", event, oClass, method.getMethod().getName(), e);
                    } finally {
                        Thread.currentThread().setContextClassLoader(appClassLoader);
                    }

                }
            }
            eventClass = eventClass.getSuperclass();

        }

    }

    /**
     * 异步事件处理
     *
     * @param event
     */
    private void dealEventAsync(IEvent event) {
        AsyncEvent asyncEvent = event.getClass().getAnnotation(AsyncEvent.class);
        if (asyncEvent == null || !asyncEvent.async()) {
            log.warn("{} 事件不是异步事件，即将转入同步事件处理器", event.getClass());
            dealEventSync(event);
            return;
        }

        log.error("异步事件处理器不可用，错误代码：未实现");

    }

    @Override
    public void post(IEvent event) {
        if (!checkEvent(event)) return;
        ForcePost forcePost = event.getClass().getAnnotation(ForcePost.class);
        if (forcePost != null && forcePost.mode() == PostMode.SYNC) {
            postSync(event);
            return;
        }
        eventsQueue.offer(event);
        log.debug("{} 事件已放入队列，事件源：{}", event.getClass(), event.getPayload());
    }

    @Override
    public void postSync(IEvent event) {
        if (!checkEvent(event)) return;
        ForcePost forcePost = event.getClass().getAnnotation(ForcePost.class);
        if (forcePost != null && forcePost.mode() == PostMode.ASYNC) {
            post(event);
            return;
        }
        log.debug("{} 事件准备分发，事件源：{}", event.getClass(), event.getPayload());
        synchronized (lock) {
            dealEventSync(event);
        }
    }

    @Override
    public void dead(Class<?> clazz) {
        deadListener.add(clazz);
    }

    private boolean checkEvent(IEvent event) {
        if (event == null) {
            log.debug("POST接收到一个空值");
            return false;
        }
        if (event.isPrevent()) {
            log.debug("POST接收到死亡事件", new IllegalArgumentException());
            return false;
        }
        return true;
    }

    @Override
    public void onLoad() {
        // 初始化线程池
        thread.setDaemon(false);
        thread.start();
    }

    @Override
    public void onDestroy() {
        stop = true;
    }

    @Override
    public void run() {

        while (!stop) {
            synchronized (lock) {
                // 移除掉上一次死亡的 Listener

                while (!deadListener.isEmpty()) {
                    LinkedHashSet<Class<? extends IEvent>> update_set = new LinkedHashSet<>();
                    Class<?> beansClass = deadListener.remove();
                    if (beans_map.containsKey(beansClass)) {
                        LinkedList<SubscriberMethod> methods = beans_map.get(beansClass);
                        methods.forEach((method) -> {
                            update_set.add(method.getEventType());
                            subscribers_map.get(method.getEventType()).remove(method);
                        });
                        beans_map.remove(beansClass);
                        log.debug("{} 死亡的监听器", beansClass);
                        update_set.forEach(this::sort_list);
                    }
                }

                // 执行上一次申请的操作
                while (!operatorQueue.isEmpty()) {
                    EventTask task = operatorQueue.remove();
                    if (task.getTask() == D_REG) {
                        dUnregister(task.getContext());
                    } else {
                        dRegister(task.getContext());
                    }
                }

                // 处理事件
                while (!eventsQueue.isEmpty()) {

                    IEvent event = eventsQueue.remove();
                    dealEventSync(event);

                }
            }

        }
        log.warn("事件总线已停止！(若在应用程序终止阶段收到该警告，请忽略)");
    }

    @Data
    @AllArgsConstructor
    static class EventTask {
        private int task;
        private BeanContext context;
    }
}
