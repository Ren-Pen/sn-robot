package com.slimenano.framework.event.impl.plugin;

import com.slimenano.framework.event.impl.ISysEvent;
import com.slimenano.sdk.event.PostMode;
import com.slimenano.sdk.event.annotations.ForcePost;
import com.slimenano.sdk.plugin.PluginInformation;

/**
 * 插件预加载事件
 */


@ForcePost(mode = PostMode.ASYNC)
public class PluginPreLoadEvent extends ISysEvent<PluginInformation> {

    // 强制同步递送，因为要判断加载事件是否被取消

    public PluginPreLoadEvent(PluginInformation source) {
        super(source);
    }


}
