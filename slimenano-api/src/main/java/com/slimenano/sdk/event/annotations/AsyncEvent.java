package com.slimenano.sdk.event.annotations;

import java.lang.annotation.*;

/**
 * 异步事件，事件通道将会以异步的形式分发事件，异步事件不可被阻止
 * 异步事件的子事件也是异步事件，除非手动设置async=false
 * 暂不支持
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface AsyncEvent {
    boolean async() default true;
}
