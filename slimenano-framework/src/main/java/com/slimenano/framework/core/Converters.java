package com.slimenano.framework.core;


import lombok.extern.slf4j.Slf4j;
import com.slimenano.framework.commons.ClassUtils;
import com.slimenano.sdk.framework.Context;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.converters.Converter;
import com.slimenano.sdk.logger.Marker;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SystemInstance
@Slf4j
@Marker("类型转换器")
public class Converters implements InitializationBean {

    private HashMap<Class, Converter> forwardMap;
    private HashMap<Class, Converter> reverseMap;

    private LinkedHashMap<Class, Converter> cacheLRU;

    @Mount
    private Context context;

    @Override
    public void onLoad() throws Exception {
        List<Converter> beans = context.getBeans(Converter.class);
        forwardMap = new HashMap<>(beans.size());
        reverseMap = new HashMap<>(beans.size());
        int capacity = (int) (beans.size() * 2.5);
        cacheLRU = new LinkedHashMap<Class, Converter>(capacity) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };

        for (Converter converter : beans) {
            Class<?> t = ClassUtils.getGenerateClass(converter.getClass(), 0);
            Class<?> r = ClassUtils.getGenerateClass(converter.getClass(), 1);
            if (forwardMap.containsKey(t)) {
                log.warn("{} 冲突的转换类型，目标转换器被覆盖 源：{} 目标：{}", t, converter.getClass(), forwardMap.get(t).getClass());
                continue;
            }

            forwardMap.put(t, converter);
            reverseMap.put(r, converter);

            log.debug("{} 转换器已装载", converter.getClass());


        }

    }

    @Override
    public void onDestroy() throws Exception {

    }


    private Converter findAndCached(HashMap<Class, Converter> storage, Class clazz) {
        if (storage.containsKey(clazz)) {
            return storage.get(clazz);
        }
        if (cacheLRU.containsKey(clazz)) {
            return cacheLRU.get(clazz);
        }
        for (Class aClass : storage.keySet()) {
            if (aClass.isAssignableFrom(clazz)) {
                Converter value = storage.get(aClass);
                if (cacheLRU.containsKey(clazz)) {
                    log.debug("缓存被覆盖 源：{}", clazz);
                }
                cacheLRU.put(clazz, value);
                return value;
            }
        }

        return null;
    }


    public <T, R, B extends BaseRobot> R convert(T object, Class<R> clazz) {
        Converter converter = findAndCached(forwardMap, object.getClass());
        if (converter == null) {
            log.debug("{} 无该类型转换器", object.getClass());
        }
        try {
            Object o = converter.convert(object);
            if (clazz.isAssignableFrom(o.getClass())) {
                return (R) o;
            } else {
                cacheLRU.remove(object.getClass());
                log.debug("{} 转换得到了错误的返回类型：{}，缓存被移除。", object.getClass(), o.getClass());
            }
        } catch (Throwable t) {
            cacheLRU.remove(object.getClass());
            log.debug("{} 转换异常，移除缓存", object.getClass(), t);
        }
        return null;

    }

    public <T, R, B extends BaseRobot> T reverseConvert(R object, Class<T> clazz) {
        Converter converter = findAndCached(reverseMap, object.getClass());
        if (converter == null) {
            log.debug("{} 无该类型转换器", object.getClass());
        }
        try {
            Object o = converter.reverse_convert(object);
            if (clazz.isAssignableFrom(o.getClass())) {
                return (T) o;
            } else {
                cacheLRU.remove(object.getClass());
                log.debug("{} 转换得到了错误的返回类型：{}，缓存被移除。", object.getClass(), o.getClass());
            }
        } catch (Throwable t) {
            cacheLRU.remove(object.getClass());
            log.debug("{} 转换异常，移除缓存", object.getClass(), t);
        }
        return null;
    }


}
