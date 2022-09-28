package top.bioelectronic.sdk.framework.annotations;

import java.lang.annotation.*;

/**
 * 代表了bean的实例不应该由Context创建而是来自类中的静态变量中
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Belong
@Inherited
public @interface FromInstance {

    String field();

}
