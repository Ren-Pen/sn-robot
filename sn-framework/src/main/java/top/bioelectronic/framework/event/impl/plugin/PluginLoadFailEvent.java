package top.bioelectronic.framework.event.impl.plugin;

import top.bioelectronic.sdk.event.IEvent;
import top.bioelectronic.sdk.event.PostMode;
import top.bioelectronic.sdk.event.annotations.ForcePost;
import top.bioelectronic.sdk.plugin.PluginInformation;

@ForcePost(mode = PostMode.ASYNC)
public class PluginLoadFailEvent extends IEvent<PluginInformation> {

    public PluginLoadFailEvent(PluginInformation payload) {
        super(payload);
    }

}
