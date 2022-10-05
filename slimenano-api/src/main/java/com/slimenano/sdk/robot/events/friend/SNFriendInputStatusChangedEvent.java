package com.slimenano.sdk.robot.events.friend;

import com.slimenano.sdk.robot.contact.user.SNFriend;
import lombok.Getter;
import com.slimenano.sdk.event.IEvent;

public class SNFriendInputStatusChangedEvent extends IEvent<SNFriend> {

    @Getter
    private final boolean inputting;

    public SNFriendInputStatusChangedEvent(SNFriend source, boolean inputting) {
        super(source);
        this.inputting = inputting;
    }

}
