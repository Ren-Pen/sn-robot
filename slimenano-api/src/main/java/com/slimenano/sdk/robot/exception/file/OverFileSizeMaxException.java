package com.slimenano.sdk.robot.exception.file;

import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 上传文件过大异常
 */
public class OverFileSizeMaxException extends RobotException {
    public OverFileSizeMaxException() {
        super();
    }

    public OverFileSizeMaxException(String message) {
        super(message);
    }

    public OverFileSizeMaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverFileSizeMaxException(Throwable cause) {
        super(cause);
    }

    protected OverFileSizeMaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
