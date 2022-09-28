package top.bioelectronic.sdk.framework.annotations;

import java.lang.annotation.*;

/**
 * 为自动装填标识一个字段的类型是来自当前类的泛型，slot：表示是泛型列表中的第几个类型：<br/><br/>
 * TestGenericClass&lt;T, R, I&gt;{<br/><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;{@link Mount &#64;Autowired}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;{@link GenericClassField &#64;GenericClassField(slot = 0)}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;T tFiled;<br/><br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;{@link Mount &#64;Autowired}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;{@link GenericClassField &#64;GenericClassField(slot = 1)}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;R tFiled;<br/><br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;{@link Mount &#64;Autowired}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;{@link GenericClassField &#64;GenericClassField(slot = 2)}<br/>
 *     &nbsp;&nbsp;&nbsp;&nbsp;I tFiled;<br/><br/>
 * }<br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface GenericClassField {
    int slot() default 0;
}
