package top.bioelectronic.sdk.robot.events.friend;

import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;

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
