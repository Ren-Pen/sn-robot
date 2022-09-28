package top.bioelectronic.sdk.robot.messages.meta;

import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNMetaMessage;

@Getter
public class SNQuoteReply extends SNMetaMessage {

    private final SNMessageSource source;

    public SNQuoteReply(SNMessageSource source) {
        this.source = source;
    }
}
