package com.slimenano.sdk.plugin;

import com.slimenano.sdk.access.AccessManager;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.slimenano.sdk.event.EventChannel;

import java.io.File;

@Slf4j
@Marker("插件")
public abstract class BasePlugin implements InitializationBean {

    public static final String SDK_VERSION = "alpha-0.0.1.12";

    @Mount("dataDir")
    @Getter
    private File dataDir;

    @Mount("bot")
    @Getter
    private Robot bot;

    @Mount("information")
    @Getter
    private PluginInformation information;

    @Mount("eventChannel")
    @Getter
    private EventChannel eventChannel;

    @Mount("iGUIBridge")
    @Getter
    private IGUIBridge bridge;

    @Mount("accessManager")
    @Getter
    private AccessManager accessManager;

    public void loaded() {}

    @Override
    public void onLoad() throws Exception {

    }

    @Override
    public void onDestroy() throws Exception {
    }

    protected final void runOnPluginClassLoader(Runnable runnable){
        if (runnable == null) return;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        try{
            runnable.run();
        }catch (Throwable t){
            log.error("{} 执行时致命错误。", information.getPath(), t);
        }finally {
            Thread.currentThread().setContextClassLoader(classLoader);
        }

    }
}

