package top.bioelectronic.framework.event.impl.plugin;

import top.bioelectronic.framework.plugin.PluginMeta;
import top.bioelectronic.framework.event.impl.ISysEvent;
import top.bioelectronic.sdk.event.PostMode;
import top.bioelectronic.sdk.event.annotations.ForcePost;

/**
 * 插件加载后事件
 */
@ForcePost(mode = PostMode.ASYNC)
public class PluginLoadedEvent extends ISysEvent<PluginMeta> {

    public PluginLoadedEvent(PluginMeta source) {
        super(source);
    }

}
