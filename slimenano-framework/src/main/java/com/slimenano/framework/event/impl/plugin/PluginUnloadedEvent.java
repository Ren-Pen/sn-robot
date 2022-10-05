package com.slimenano.framework.event.impl.plugin;

import com.slimenano.framework.plugin.PluginMeta;
import com.slimenano.framework.event.impl.ISysEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.event.annotations.ForcePost;

@ForcePost(mode = PostMode.ASYNC)
public class PluginUnloadedEvent extends ISysEvent<PluginMeta> {

    public PluginUnloadedEvent(PluginMeta source) {
        super(source);
    }

}
