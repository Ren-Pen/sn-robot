package com.slimenano.sdk.framework.annotations;

import java.lang.annotation.*;

@Documented
@Repeatable(InstanceAliases.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Instance
public @interface InstanceAlias {
    String alias();
}
