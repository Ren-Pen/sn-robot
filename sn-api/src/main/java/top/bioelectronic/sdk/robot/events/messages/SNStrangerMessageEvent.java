package top.bioelectronic.sdk.robot.events.messages;

import top.bioelectronic.sdk.robot.contact.user.SNStranger;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import lombok.Getter;

@Getter
public class SNStrangerMessageEvent extends SNMessageEvent {

    private final int time;

    public SNStrangerMessageEvent(SNMessageChain chain, SNStranger from, int time) {
        super(from, chain);
        this.time = time;
    }

    @Override
    public SNStranger getFrom() {
        return (SNStranger) super.getFrom();
    }
}
