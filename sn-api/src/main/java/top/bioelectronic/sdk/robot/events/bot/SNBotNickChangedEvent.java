package top.bioelectronic.sdk.robot.events.bot;

import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;

@Getter
public class SNBotNickChangedEvent extends IEvent<Object> {

    private final String from;
    private final String to;

    public SNBotNickChangedEvent(String from, String to) {
        super(null);
        this.from = from;
        this.to = to;
    }

}
