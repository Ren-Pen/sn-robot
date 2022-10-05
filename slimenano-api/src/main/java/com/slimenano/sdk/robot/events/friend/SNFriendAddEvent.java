package com.slimenano.sdk.robot.events.friend;

import com.slimenano.sdk.robot.contact.user.SNFriend;
import com.slimenano.sdk.event.IEvent;

public class SNFriendAddEvent extends IEvent<SNFriend> {



    public SNFriendAddEvent(SNFriend friend) {
        super(friend);
    }

}
