package top.bioelectronic.framework.event.impl.plugin;

import top.bioelectronic.framework.event.impl.ISysEvent;
import top.bioelectronic.sdk.event.PostMode;
import top.bioelectronic.sdk.event.annotations.ForcePost;
import top.bioelectronic.sdk.plugin.PluginInformation;

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
