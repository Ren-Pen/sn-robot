package top.bioelectronic.sdk.framework.converters;

import top.bioelectronic.sdk.framework.annotations.GenericClassField;
import top.bioelectronic.sdk.framework.annotations.Instance;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.framework.core.BaseRobot;
import top.bioelectronic.framework.core.Converters;

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
