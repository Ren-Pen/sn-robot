package com.slimenano.sdk.plugin;

import com.slimenano.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Instance
public @interface Plugin {
}
