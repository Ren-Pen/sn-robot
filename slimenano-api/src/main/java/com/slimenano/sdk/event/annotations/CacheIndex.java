package com.slimenano.sdk.event.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明当前的 ContextBean 拥有或可以建立一个事件方法索引缓存，该缓存可以加速事件总线注册的速度
 * @Field version 索引缓存的版本，若版本与索引文件版本不一致，则将会重新创建索引文件
 * @Warn 虽然在一些异常的情况下事件方法索引可以自动重建，但新增事件接收方法就是一个例外，因为方法的增加并不会让事件总线感知到任何异常，此时需要开发者手动设置新的版本号强制事件总线更新索引。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CacheIndex {
    int version() default Integer.MAX_VALUE;
}
