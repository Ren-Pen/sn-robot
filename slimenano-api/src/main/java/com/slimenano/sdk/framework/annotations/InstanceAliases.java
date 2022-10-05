package com.slimenano.sdk.framework.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Instance
public @interface InstanceAliases {
    InstanceAlias[] value();
}
