package com.slimenano.sdk.access;

import com.slimenano.sdk.plugin.PluginInformation;

public interface AccessManager {
    boolean hasAccess(PluginInformation information, Permission permission);

    boolean canAccess(PluginInformation information, Permission[] permissions);

    boolean useAccess(PluginInformation information, Permission permission);
}
