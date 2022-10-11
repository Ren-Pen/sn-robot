package com.slimenano.sdk.robot.exception;

/**
 * 机器人异常，总异常
 */
public abstract class RobotException extends RuntimeException{
    public RobotException() {
        super();
    }

    public RobotException(String message) {
        super(message);
    }

    public RobotException(String message, Throwable cause) {
        super(message, cause);
    }

    public RobotException(Throwable cause) {
        super(cause);
    }

    protected RobotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
