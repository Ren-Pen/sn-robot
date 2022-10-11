package com.slimenano.sdk.robot.exception.unsupported;

import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 方法或操作不支持时抛出的异常
 */
public abstract class UnsupportedException extends RobotException {

    public UnsupportedException() {
        super();
    }

    public UnsupportedException(String message) {
        super(message);
    }

    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
