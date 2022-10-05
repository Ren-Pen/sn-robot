package com.slimenano.framework.event.impl.plugin;

import com.slimenano.sdk.event.IEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.event.annotations.ForcePost;
import com.slimenano.sdk.plugin.PluginInformation;

@ForcePost(mode = PostMode.ASYNC)
public class PluginLoadFailEvent extends IEvent<PluginInformation> {

    public PluginLoadFailEvent(PluginInformation payload) {
        super(payload);
    }

}
