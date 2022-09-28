package top.bioelectronic.framework.core;


import top.bioelectronic.sdk.framework.Context;
import top.bioelectronic.sdk.framework.InitializationBean;
import top.bioelectronic.sdk.framework.SystemInstance;
import lombok.extern.slf4j.Slf4j;
import top.bioelectronic.framework.commons.ClassUtils;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.framework.converters.Converter;
import top.bioelectronic.sdk.logger.Marker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SystemInstance
@Slf4j
@Marker("类型转换器")
public class Converters extends ConcurrentHashMap<Class<?>, Converter> implements InitializationBean {

    private final ConcurrentHashMap<Class<?>, Converter> reverse_converters = new ConcurrentHashMap<>();

    @Mount
    private Context context;

    @Override
    public void onLoad() throws Exception {
        List<Converter> beans = context.getBeans(Converter.class);
        for (Converter converter : beans) {
            Class<?> t = ClassUtils.getGenerateClass(converter.getClass(), 0);
            if (containsKey(t)) {
                log.warn("{} 冲突的转换类型 源：{} 目标：{}", t, converter.getClass(), get(t).getClass());
                continue;
            }

            Class<?> r = ClassUtils.getGenerateClass(converter.getClass(), 1);

            log.debug("{} 转换器已装载", converter.getClass());
            put(t, converter);
            reverse_converters.put(r, converter);
        }
    }

    @Override
    public void onDestroy() throws Exception {

    }

    public <T, R, B extends BaseRobot> R convert(T object, Class<R> clazz) {

        if (object == null) return null;
        Class<?> typeClass = object.getClass();
        Class<?> tempC = typeClass;

        try {
            while (tempC != Object.class) {
                if (containsKey(tempC)) {
                    Converter<T, R, B> converter = get(tempC);
                    if (clazz.isAssignableFrom(ClassUtils.getGenerateClass(converter.getClass(), 1))) {
                        if (!containsKey(typeClass)) {
                            put(typeClass, converter);
                        }
                        return converter.convert(object);
                    }
                }
                for (Class<?> iterface : tempC.getInterfaces()) {
                    while (iterface != null) {
                        if (containsKey(iterface)) {
                            Converter<T, R, B> converter = get(iterface);
                            if (clazz.isAssignableFrom(ClassUtils.getGenerateClass(converter.getClass(), 1))) {
                                // 找到了目标，加入缓存
                                put(typeClass, converter);
                                return converter.convert(object);
                            }
                        }
                        iterface = iterface.getSuperclass();
                    }
                }
                tempC = tempC.getSuperclass();
            }
        }catch (Throwable t){
            log.debug("{} 转换异常", typeClass, t);
        }

        log.debug("{} 无该类型转换器", typeClass);


        return null;


    }

    public <T, R, B extends BaseRobot> T reverseConvert(R object, Class<T> clazz){
        if (object == null) return null;
        Class<?> typeClass = object.getClass();
        Class<?> tempC = typeClass;

        try {
            while (tempC != Object.class) {
                if (reverse_converters.containsKey(tempC)) {
                    Converter<T, R, B> converter = reverse_converters.get(tempC);
                    if (clazz.isAssignableFrom(ClassUtils.getGenerateClass(converter.getClass(), 0))) {
                        if (!reverse_converters.containsKey(typeClass)) {
                            reverse_converters.put(typeClass, converter);
                        }
                        return converter.reverse_convert(object);
                    }
                }
                for (Class<?> iterface : tempC.getInterfaces()) {
                    while (iterface != null) {
                        if (reverse_converters.containsKey(iterface)) {
                            Converter<T, R, B> converter = reverse_converters.get(iterface);
                            if (clazz.isAssignableFrom(ClassUtils.getGenerateClass(converter.getClass(), 0))) {
                                // 找到了目标，加入缓存
                                if (!reverse_converters.containsKey(typeClass)) {
                                    reverse_converters.put(typeClass, converter);
                                }
                                return converter.reverse_convert(object);
                            }
                        }
                        iterface = iterface.getSuperclass();
                    }
                }
                tempC = tempC.getSuperclass();
            }
        }catch (Throwable t){
            log.debug("{} 转换异常", typeClass, t);
        }

        log.debug("{} 无该类型转换器", typeClass);


        return null;



    }





}
