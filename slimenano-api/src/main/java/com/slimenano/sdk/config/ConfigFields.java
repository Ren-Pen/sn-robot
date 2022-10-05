package com.slimenano.sdk.config;

import java.lang.annotation.*;

/**
 * 序列化的字段
 * 当配置该注解时，将仅会序列化列表中的字段
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigFields {

    String[] fieldName();
    Class<?>[] fieldType();

}
