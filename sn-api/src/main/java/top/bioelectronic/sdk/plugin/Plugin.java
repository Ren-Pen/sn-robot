package top.bioelectronic.sdk.plugin;

import top.bioelectronic.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Instance
public @interface Plugin {
}
