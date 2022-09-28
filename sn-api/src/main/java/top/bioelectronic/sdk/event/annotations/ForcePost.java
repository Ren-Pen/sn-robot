package top.bioelectronic.sdk.event.annotations;

import top.bioelectronic.sdk.event.PostMode;

import java.lang.annotation.*;

/**
 * 强制事件只能使用同步递送
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ForcePost {

    PostMode mode() default PostMode.ASYNC;

}
