package top.bioelectronic.sdk.robot.events;

import lombok.Getter;
import top.bioelectronic.sdk.event.IEvent;
import top.bioelectronic.sdk.event.PostMode;
import top.bioelectronic.sdk.event.annotations.ForcePost;

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
