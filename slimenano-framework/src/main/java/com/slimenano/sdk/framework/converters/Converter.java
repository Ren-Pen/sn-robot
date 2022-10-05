package com.slimenano.sdk.framework.converters;

import com.slimenano.sdk.framework.annotations.GenericClassField;
import com.slimenano.sdk.framework.annotations.Instance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.framework.core.BaseRobot;
import com.slimenano.framework.core.Converters;

/**
 * 转换器，将Mirai对象转换为其他对象
 */
@Instance
public abstract class Converter<T, R, B extends BaseRobot> {

    @Mount
    protected Converters converters;

    @Mount
    @GenericClassField(slot = 2)
    protected B robot;

    public abstract R convert(T t);

    /**
     * 逆转换
     * @param r
     * @return
     */
    public T reverse_convert(R r){
        return null;
    }

}
