package com.slimenano.sdk.framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Belong
@Inherited
public @interface Instance {
}
