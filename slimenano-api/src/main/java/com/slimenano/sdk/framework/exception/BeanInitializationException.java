package com.slimenano.sdk.framework.exception;

public class BeanInitializationException extends BeanException{
    public BeanInitializationException() {
    }

    public BeanInitializationException(String message) {
        super(message);
    }

    public BeanInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanInitializationException(Throwable cause) {
        super(cause);
    }

    public BeanInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
