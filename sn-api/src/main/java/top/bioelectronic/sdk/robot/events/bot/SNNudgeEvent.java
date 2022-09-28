package top.bioelectronic.sdk.robot.events.bot;

import top.bioelectronic.sdk.common.Nullable;
import top.bioelectronic.sdk.robot.contact.SNContact;
import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;

@Getter
public class SNNudgeEvent extends IEvent<Object> {

    @Nullable
    private final SNContact from; // 如果为bot本身，则为null

    @Nullable
    private final SNContact target; // 如果为bot本身，则为null

    private final String action;
    private final String suffix;

    public SNNudgeEvent(SNContact from, SNContact target, String action, String suffix) {
        super(null);
        this.from = from;
        this.target = target;
        this.action = action;
        this.suffix = suffix;
    }

}
