package com.slimenano.sdk.robot.events.bot;

import com.slimenano.sdk.common.Nullable;
import com.slimenano.sdk.robot.contact.SNContact;
import lombok.Getter;
import com.slimenano.sdk.event.IEvent;

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
