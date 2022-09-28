package top.bioelectronic.sdk.framework;

import top.bioelectronic.sdk.framework.exception.BeanRepeatNameException;
import top.bioelectronic.sdk.framework.exception.GetBeanException;

import java.io.IOException;
import java.util.List;

public interface Context {

    int CREATED = 0;
    int SCANNED = 1;
    int INITIALIZED = 2;
    int WIRED = 3;
    int LOADED = 4;
    int BEFORE_DESTROY = 5;
    int DESTROYED = 6;

    int getStatus();

    ClassLoader getBeanClassLoader();

    void storeConfiguration() throws IOException;

    void addBean(String name, Object o) throws BeanRepeatNameException;

    Object getBean(String name) throws GetBeanException;

    <T> T getBeanOrNull(Class<T> clazz);

    <T> List<T> getBeans(Class<T> clazz);

    <T> T getBean(String name, Class<T> clazz) throws GetBeanException;

    <T> T getBean(Class<T> clazz) throws GetBeanException;

    List<Object> getBeans();

    <T> List<T> getBeans(String regex, Class<T> clazz);

    List<Object> getBeans(String regex);
}
