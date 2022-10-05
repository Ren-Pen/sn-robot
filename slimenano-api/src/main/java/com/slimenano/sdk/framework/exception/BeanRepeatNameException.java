package com.slimenano.sdk.framework.exception;

public class BeanRepeatNameException extends BeanException{
    public BeanRepeatNameException() {
    }

    public BeanRepeatNameException(String message) {
        super(message);
    }

    public BeanRepeatNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanRepeatNameException(Throwable cause) {
        super(cause);
    }

    public BeanRepeatNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
