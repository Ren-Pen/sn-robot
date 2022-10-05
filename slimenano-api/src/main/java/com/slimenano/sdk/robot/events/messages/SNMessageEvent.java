package com.slimenano.sdk.robot.events.messages;

import com.slimenano.sdk.robot.contact.SNContact;
import lombok.Getter;
import com.slimenano.sdk.event.IEvent;
import com.slimenano.sdk.robot.messages.SNMessageChain;

public class SNMessageEvent extends IEvent<SNMessageChain> {

    @Getter
    private final SNContact from;

    public SNMessageEvent(SNContact from, SNMessageChain chain) {
        super(chain);
        this.from = from;
    }

}
