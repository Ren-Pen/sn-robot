package com.slimenano.sdk.robot.exception.permission;

import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.robot.exception.RobotException;

/**
 * 插件无执行权限异常，需要 AccessManager 授权
 */
public class NoOperationPermissionException extends RobotException {

    /**
     * 需要授权的权限
     */
    private Permission[] require;

    public NoOperationPermissionException(Permission[] require) {
        this.require = require;
    }

    @Override
    public String getMessage() {
        return "需要权限：" +  Permission.toString("[%s] %s%n", require);
    }
}
