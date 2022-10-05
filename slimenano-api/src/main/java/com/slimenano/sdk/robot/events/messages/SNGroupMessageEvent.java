package com.slimenano.sdk.robot.events.messages;

import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.user.SNMember;
import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNMessageChain;

@Getter
public class SNGroupMessageEvent extends SNMessageEvent {

    private final int time;
    private final SNMember sender;

    public SNGroupMessageEvent(SNMessageChain chain, SNGroup from, SNMember sender, int time) {
        super(from, chain);
        this.time = time;
        this.sender = sender;
    }

    @Override
    public SNGroup getFrom() {
        return (SNGroup) super.getFrom();
    }
}
