package top.bioelectronic.sdk.robot.events.messages;

import top.bioelectronic.sdk.robot.contact.user.SNMember;
import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;

@Getter
public class SNGroupTempMessageEvent extends SNMessageEvent {

    private final int time;

    public SNGroupTempMessageEvent(SNMessageChain chain, SNMember sender, int time) {
        super(sender, chain);
        this.time = time;
    }

    @Override
    public SNMember getFrom() {
        return (SNMember) super.getFrom();
    }
}
