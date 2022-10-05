package com.slimenano.sdk.robot.events.friend;

import com.slimenano.sdk.robot.contact.user.SNFriend;
import lombok.Getter;
import com.slimenano.sdk.event.IEvent;

@Getter
public class SNFriendRemarkChangeEvent extends IEvent<SNFriend> {

    private final String oldRemark;
    private final String newRemark;

    public SNFriendRemarkChangeEvent(SNFriend friend, String oldRemark, String newRemark) {
        super(friend);
        this.oldRemark = oldRemark;
        this.newRemark = newRemark;
    }

}
