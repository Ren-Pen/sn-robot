package top.bioelectronic.sdk.framework.annotations;

import top.bioelectronic.sdk.access.Access;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Inherited
public @interface AccessControl {
    Access[] require() default {};
}
