package com.slimenano.sdk.robot.messages.meta;

import com.slimenano.sdk.core.Robot;
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

    public boolean recall(Robot robot){
        return robot.recall(this);
    }
}
