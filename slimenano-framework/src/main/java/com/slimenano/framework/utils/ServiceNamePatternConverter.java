package com.slimenano.framework.utils;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import com.slimenano.framework.commons.ClassUtils;
import com.slimenano.sdk.logger.Marker;

@Plugin(name = "ServiceNamePatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"s", "serviceName"})
public class ServiceNamePatternConverter extends LogEventPatternConverter {

    private static final ServiceNamePatternConverter INSTANCE = new ServiceNamePatternConverter();

    protected ServiceNamePatternConverter() {
        super("serviceName", "serviceName");
    }

    public static ServiceNamePatternConverter newInstance(final String[] options) { return INSTANCE; }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null){
                loader = this.getClass().getClassLoader();
            }
            Class<?> aClass = loader.loadClass(event.getSource().getClassName());
            Marker marker = ClassUtils.getAnnotation(aClass, Marker.class);
            if (marker != null){
                toAppendTo.append(marker.value());
            }else{
                toAppendTo.append("<unknown>");
            }
        } catch (ClassNotFoundException ignore) {
            toAppendTo.append("<unknown>");
        }
    }
}
