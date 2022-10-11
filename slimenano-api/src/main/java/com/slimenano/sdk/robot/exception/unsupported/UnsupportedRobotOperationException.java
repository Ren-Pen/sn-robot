package com.slimenano.sdk.robot.exception.unsupported;

/**
 * 未实现操作异常，若机器人核心未实现BaseRobot接口中的方法，则会抛出该异常
 */
public class UnsupportedRobotOperationException extends UnsupportedException{

    public UnsupportedRobotOperationException() {
        super();
    }

    public UnsupportedRobotOperationException(String message) {
        super(message);
    }

    public UnsupportedRobotOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedRobotOperationException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedRobotOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
