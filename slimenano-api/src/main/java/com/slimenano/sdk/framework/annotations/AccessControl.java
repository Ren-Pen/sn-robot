package com.slimenano.sdk.framework.annotations;

import com.slimenano.sdk.access.Permission;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Inherited
public @interface AccessControl {
    Permission[] require() default {};
    boolean status() default false;
}
