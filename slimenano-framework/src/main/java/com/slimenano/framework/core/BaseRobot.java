package com.slimenano.framework.core;

import lombok.extern.slf4j.Slf4j;
import com.slimenano.framework.config.RobotConfiguration;
import com.slimenano.framework.event.EventChannelImpl;
import com.slimenano.framework.event.impl.bot.LoginEvent;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.BeanContext;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.robot.messages.SNContentMessage;
import com.slimenano.sdk.robot.messages.SNMessageChain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SystemInstance
@Slf4j
@Marker("ROBOT")
public abstract class BaseRobot implements Robot, InitializationBean {

    @Mount
    protected RobotConfiguration robotConfiguration;

    @Mount
    protected EventChannelImpl eventChannel;

    @Mount
    protected BeanContext context;

    @Mount
    protected Converters converters;

    @Mount
    protected IGUIBridge bridge;

    protected final boolean netCheck() {
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + "www.baidu.com");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();

            if (!sb.toString().equals("")) {
                connect = sb.toString().indexOf("TTL") > 0;
            }
        } catch (IOException ignored) {

        }
        return connect;
    }

    protected abstract boolean getStatus();

    protected abstract void initInstance();

    protected abstract void toLogin() throws Exception;

    protected abstract void close() throws Exception;

    public final void login() {
        eventChannel.post(new LoginEvent("准备登录", LoginEvent.INFO));
        if (!netCheck()) {
            log.error("网络未连接，请检查网络设置！");
            eventChannel.post(new LoginEvent("网络未连接，请检查网络设置", LoginEvent.ERROR));
            return;
        }
        if (getStatus()) {
            log.error("重复的登录！");
            eventChannel.post(new LoginEvent("重复的登录", LoginEvent.ERROR));
            return;
        }
        eventChannel.post(new LoginEvent("初始化实例中...", LoginEvent.INFO));
        initInstance();

        eventChannel.post(new LoginEvent("登录中...（请注意弹窗）", LoginEvent.INFO));
        try {
            toLogin();
            log.debug("登录成功");
            eventChannel.post(new LoginEvent("登录成功", LoginEvent.SUCCESS));
        } catch (Throwable t) {
            log.error("登录失败", t);
            eventChannel.post(new LoginEvent("登录失败，请检查日志", LoginEvent.ERROR, t));

        }

    }

    protected boolean isIllegalMessageChain(SNMessageChain chain) {
        return chain.isEmpty() || (chain.get(SNContentMessage.class).length == 0);
    }

    @Override
    public void onLoad() throws Exception {

    }

    @Override
    public final void onDestroy() throws Exception {
        close();
    }
}
