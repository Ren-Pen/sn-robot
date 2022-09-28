package top.bioelectronic.sdk.framework;

import top.bioelectronic.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Instance
@ISystem
public @interface SystemInstance {
}
