package com.demo;

import top.bioelectronic.sdk.config.ConfigLocation;
import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.plugin.BasePlugin;
import top.bioelectronic.sdk.plugin.Plugin;

@Plugin
@ConfigLocation(location = "config")
public class DemoPlugin extends BasePlugin {

    @Mount
    Robot robot;

}
