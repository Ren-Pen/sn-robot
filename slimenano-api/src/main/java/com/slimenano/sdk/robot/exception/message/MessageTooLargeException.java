package com.slimenano.sdk.robot.exception.message;

import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 消息太长异常
 */
public class MessageTooLargeException extends RobotException {
    public MessageTooLargeException() {
    }

    public MessageTooLargeException(String message) {
        super(message);
    }

    public MessageTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageTooLargeException(Throwable cause) {
        super(cause);
    }

    public MessageTooLargeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
