package top.bioelectronic.framework.event.impl.plugin;

import top.bioelectronic.framework.plugin.PluginMeta;
import top.bioelectronic.framework.event.impl.ISysEvent;
import top.bioelectronic.sdk.event.PostMode;
import top.bioelectronic.sdk.event.annotations.ForcePost;

@ForcePost(mode = PostMode.ASYNC)
public class PluginUnloadedEvent extends ISysEvent<PluginMeta> {

    public PluginUnloadedEvent(PluginMeta source) {
        super(source);
    }

}
