package top.bioelectronic.sdk.robot.events.messages;

import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.user.SNMember;
import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;

@Getter
public class SNGroupMessageEvent extends SNMessageEvent {

    private final int time;
    private final SNMember sender;

    public SNGroupMessageEvent(SNMessageChain chain, SNGroup from, SNMember sender, int time) {
        super(from, chain);
        this.time = time;
        this.sender = sender;
    }

    @Override
    public SNGroup getFrom() {
        return (SNGroup) super.getFrom();
    }
}
