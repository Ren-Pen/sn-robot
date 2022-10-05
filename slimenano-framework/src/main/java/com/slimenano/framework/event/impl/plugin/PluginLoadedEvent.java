package com.slimenano.framework.event.impl.plugin;

import com.slimenano.framework.plugin.PluginMeta;
import com.slimenano.framework.event.impl.ISysEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.event.annotations.ForcePost;

/**
 * 插件加载后事件
 */
@ForcePost(mode = PostMode.ASYNC)
public class PluginLoadedEvent extends ISysEvent<PluginMeta> {

    public PluginLoadedEvent(PluginMeta source) {
        super(source);
    }

}
