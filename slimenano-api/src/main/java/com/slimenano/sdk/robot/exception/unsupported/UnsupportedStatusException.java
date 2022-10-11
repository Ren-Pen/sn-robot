package com.slimenano.sdk.robot.exception.unsupported;

import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 机器人当前状态不支持执行该操作时抛出
 * 当通过status查询机器人的状态为false时这个异常将会自动抛出！
 */
public class UnsupportedStatusException extends RobotException {
    public UnsupportedStatusException() {
        super("机器人当前状态异常！无法执行该操作！【这个异常通常为离线或者未登录时抛出】");
    }
}
