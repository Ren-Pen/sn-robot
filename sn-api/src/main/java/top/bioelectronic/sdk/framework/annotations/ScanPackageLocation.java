package top.bioelectronic.sdk.framework.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface ScanPackageLocation {

    String value();

}
