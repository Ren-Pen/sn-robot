package top.bioelectronic.sdk.robot.events.messages;

import top.bioelectronic.sdk.robot.contact.SNContact;
import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;

public class SNMessageEvent extends IEvent<SNMessageChain> {

    @Getter
    private final SNContact from;

    public SNMessageEvent(SNContact from, SNMessageChain chain) {
        super(chain);
        this.from = from;
    }

}
