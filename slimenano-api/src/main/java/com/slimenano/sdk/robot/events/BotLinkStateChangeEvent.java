package com.slimenano.sdk.robot.events;

import lombok.Getter;
import com.slimenano.sdk.event.IEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.event.annotations.ForcePost;

@ForcePost(mode = PostMode.ASYNC)
public class BotLinkStateChangeEvent extends IEvent<Object> {

    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;
    public static final int MSF_OFFLINE = 2;
    public static final int FORCE = 3;

    @Getter
    private final int state;

    public BotLinkStateChangeEvent(int state) {
        super(null);
        this.state = state;
    }


}
