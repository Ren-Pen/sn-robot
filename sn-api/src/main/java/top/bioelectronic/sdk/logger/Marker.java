package top.bioelectronic.sdk.logger;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Marker {
    String value();
}
