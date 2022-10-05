package com.slimenano.sdk.framework;

import com.slimenano.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Instance
@ISystem
public @interface SystemInstance {
}
