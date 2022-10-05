package com.slimenano.sdk.framework.annotations;

import com.slimenano.sdk.access.Access;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Inherited
public @interface AccessControl {
    Access[] require() default {};
}
