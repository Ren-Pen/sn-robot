package top.bioelectronic.sdk.robot.events.messages;

import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
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
