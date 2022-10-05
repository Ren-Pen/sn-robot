package com.slimenano.sdk.event.annotations;

import com.slimenano.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Instance
public @interface EventListener {
}
