package top.bioelectronic.sdk.robot.events.friend;

import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;

public class SNFriendInputStatusChangedEvent extends IEvent<SNFriend> {

    @Getter
    private final boolean inputting;

    public SNFriendInputStatusChangedEvent(SNFriend source, boolean inputting) {
        super(source);
        this.inputting = inputting;
    }

}
