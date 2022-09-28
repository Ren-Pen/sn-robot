package top.bioelectronic.sdk.event.impl;

import top.bioelectronic.sdk.event.annotations.ForcePost;
import top.bioelectronic.sdk.event.IEvent;
import top.bioelectronic.sdk.event.PostMode;

@ForcePost(mode = PostMode.SYNC)
public class ShowMenuEvent extends IEvent<Object> {
    public ShowMenuEvent(Object source) {
        super(source);
    }



}
