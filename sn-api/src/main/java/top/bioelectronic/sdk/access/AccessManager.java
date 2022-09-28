package top.bioelectronic.sdk.access;

import top.bioelectronic.sdk.plugin.PluginInformation;

public interface AccessManager {
    boolean hasAccess(PluginInformation information, Access access);

    boolean canAccess(PluginInformation information, Access[] accesses);

    boolean useAccess(PluginInformation information, Access access);
}
