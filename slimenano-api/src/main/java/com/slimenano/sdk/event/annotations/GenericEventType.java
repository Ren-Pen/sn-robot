package com.slimenano.sdk.event.annotations;

import java.lang.annotation.*;

/**
 * 泛型只能来自于类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface GenericEventType {

    int slot() default 0;

}
