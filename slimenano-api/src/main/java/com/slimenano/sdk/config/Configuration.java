package com.slimenano.sdk.config;

import com.slimenano.sdk.framework.annotations.Instance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Instance
public @interface Configuration {
    String prefix();
}
