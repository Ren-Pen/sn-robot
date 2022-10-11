package com.slimenano.sdk.robot.exception.permission;

import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 机器人没有权限操作，或权限不足
 * 通常是在机器人不是管理员但是操作了管理员操作时抛出
 * 比如 撤回群成员消息
 */
public class BotNoPermissionException extends RobotException {

    public BotNoPermissionException() {
        super("机器人没有足够的权限！");
    }

    public BotNoPermissionException(String message) {
        super(message);
    }

    public BotNoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotNoPermissionException(Throwable cause) {
        super(cause);
    }

    protected BotNoPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
