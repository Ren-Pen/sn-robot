package com.slimenano.sdk.robot.events.messages;

import com.slimenano.sdk.robot.contact.user.SNFriend;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import lombok.Getter;

@Getter
public class SNFriendMessageEvent extends SNMessageEvent {

    private final int time;

    public SNFriendMessageEvent(SNMessageChain chain, SNFriend from, int time) {
        super(from, chain);
        this.time = time;
    }

    @Override
    public SNFriend getFrom() {
        return (SNFriend) super.getFrom();
    }

}
