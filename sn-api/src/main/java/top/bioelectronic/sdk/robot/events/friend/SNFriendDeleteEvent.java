package top.bioelectronic.sdk.robot.events.friend;

import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import top.bioelectronic.sdk.event.IEvent;

public class SNFriendDeleteEvent extends IEvent<SNFriend> {
    public SNFriendDeleteEvent(SNFriend friend) {
        super(friend);
    }
}
