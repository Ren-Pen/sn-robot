package top.bioelectronic.sdk.event.annotations;

import top.bioelectronic.sdk.framework.annotations.Instance;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Instance
public @interface EventListener {
}
