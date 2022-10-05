package com.slimenano.sdk.framework.exception;

public class GetBeanException extends BeanException{
    public GetBeanException() {
    }

    public GetBeanException(String message) {
        super(message);
    }

    public GetBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public GetBeanException(Throwable cause) {
        super(cause);
    }

    public GetBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
