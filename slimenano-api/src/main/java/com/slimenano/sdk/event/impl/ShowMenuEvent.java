package com.slimenano.sdk.event.impl;

import com.slimenano.sdk.event.annotations.ForcePost;
import com.slimenano.sdk.event.IEvent;
import com.slimenano.sdk.event.PostMode;

@ForcePost(mode = PostMode.SYNC)
public class ShowMenuEvent extends IEvent<Object> {
    public ShowMenuEvent(Object source) {
        super(source);
    }



}
