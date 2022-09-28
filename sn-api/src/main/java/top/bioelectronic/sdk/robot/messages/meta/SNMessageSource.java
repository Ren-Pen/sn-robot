package top.bioelectronic.sdk.robot.messages.meta;

import top.bioelectronic.sdk.core.Robot;
import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.SNMetaMessage;

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
