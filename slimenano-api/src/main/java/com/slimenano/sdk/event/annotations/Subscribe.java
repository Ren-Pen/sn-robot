package com.slimenano.sdk.event.annotations;

import java.lang.annotation.*;


/**
 * 表示接收事件的订阅方法
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    int priority() default 0;

}
