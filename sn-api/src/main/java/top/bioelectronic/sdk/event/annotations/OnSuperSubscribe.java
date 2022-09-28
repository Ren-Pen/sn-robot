package top.bioelectronic.sdk.event.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnSuperSubscribe {
    Class<?> superClazz();
}
