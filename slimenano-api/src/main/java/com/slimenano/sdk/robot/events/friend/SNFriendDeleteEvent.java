package com.slimenano.sdk.robot.events.friend;

import com.slimenano.sdk.robot.contact.user.SNFriend;
import com.slimenano.sdk.event.IEvent;

public class SNFriendDeleteEvent extends IEvent<SNFriend> {
    public SNFriendDeleteEvent(SNFriend friend) {
        super(friend);
    }
}
