package com.slimenano.sdk.robot.messages.meta;

import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.exception.permission.BotNoPermissionException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.SNMetaMessage;

@Getter
public class SNMessageSource extends SNMetaMessage {

    private final int[] ids;
    private final int[] internalIds;
    private final int time;
    private final long from;
    private final long target;
    private final long botId;
    private final String kind;
    private final SNMessageChain originalMessage;

    public SNMessageSource(int[] ids, int[] internalIds, int time, long from, long target, long botId, String kind, SNMessageChain originalMessage) {
        this.ids = ids;
        this.internalIds = internalIds;
        this.time = time;
        this.from = from;
        this.target = target;
        this.botId = botId;
        this.kind = kind;
        this.originalMessage = originalMessage;
    }

    /**
     * 撤回这条消息
     * @param robot 操作的robot实例
     * @throws UnsupportedRobotOperationException robot实例未实现该方法
     * @throws NoOperationPermissionException 插件没有操作权限
     * @throws BotNoPermissionException robot实例没有权限进行该操作
     */
    public void recall(Robot robot) throws UnsupportedRobotOperationException, NoOperationPermissionException, BotNoPermissionException {
        robot.recall(this);
    }
}
