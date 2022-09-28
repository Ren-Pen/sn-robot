package top.bioelectronic.sdk.robot.events.friend;

import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import top.bioelectronic.sdk.event.IEvent;

public class SNFriendAddEvent extends IEvent<SNFriend> {



    public SNFriendAddEvent(SNFriend friend) {
        super(friend);
    }

}
