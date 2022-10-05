package com.slimenano.sdk.robot.events.messages;

import com.slimenano.sdk.robot.contact.user.SNMember;
import lombok.Getter;
import com.slimenano.sdk.robot.messages.SNMessageChain;

@Getter
public class SNGroupTempMessageEvent extends SNMessageEvent {

    private final int time;

    public SNGroupTempMessageEvent(SNMessageChain chain, SNMember sender, int time) {
        super(sender, chain);
        this.time = time;
    }

    @Override
    public SNMember getFrom() {
        return (SNMember) super.getFrom();
    }
}
