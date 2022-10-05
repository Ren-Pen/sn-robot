package com.slimenano.sdk.robot.messages.meta;

import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNMetaMessage;

@Getter
public class SNQuoteReply extends SNMetaMessage {

    private final SNMessageSource source;

    public SNQuoteReply(SNMessageSource source) {
        this.source = source;
    }
}
