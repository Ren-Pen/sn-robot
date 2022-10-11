package com.slimenano.sdk.robot.contact;


import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.exception.message.MessageTooLargeException;
import com.slimenano.sdk.robot.exception.permission.BotNoPermissionException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

public interface SNContact {

    long getId();

    /**
     * 向目标发送消息
     *
     * @param robot 机器人实例
     * @param chain 消息链
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    SNMessageSource sendMessage(Robot robot, SNMessageChain chain) throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

}
