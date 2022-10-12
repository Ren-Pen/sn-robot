package com.slimenano.nscan.framework;

import com.slimenano.sdk.framework.annotations.Instance;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import lombok.SneakyThrows;

@ISystem
@Instance
public abstract class DefaultIGUIBridge implements IGUIBridge {

    private final String VERSION;
    private final String NAME;

    private DefaultIGUIBridge() {
        this.VERSION = "vABS-1.0-alpha";
        this.NAME = "Abstract Graphic User Interface Bridge";
    }

    public DefaultIGUIBridge(String VERSION, String NAME) {

        this.VERSION = VERSION;
        this.NAME = NAME;

    }

    @Override
    public boolean confirm(String title, String content, int type) {
        return false;
    }

    @Override
    public void alert(String title, String content, int type) {

    }

    @Override
    public String prompt(String title, String content, String defaultValue) {
        return null;
    }

    @SneakyThrows
    @Override
    public String getVersion() {
        return this.VERSION;
    }

    @SneakyThrows
    @Override
    public String getName() {
        return this.NAME;
    }
}
