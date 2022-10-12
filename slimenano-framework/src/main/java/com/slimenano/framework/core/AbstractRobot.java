package com.slimenano.framework.core;

import com.slimenano.framework.event.EventChannelImpl;
import com.slimenano.framework.event.impl.bot.LoginEvent;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.BeanContext;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.SystemInstance;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.framework.ui.IGUIBridge;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.robot.exception.InternalModelFailedException;
import com.slimenano.sdk.robot.exception.LoginFailedException;
import com.slimenano.sdk.robot.exception.ServerFailedException;
import com.slimenano.sdk.robot.exception.WrongPasswordException;
import com.slimenano.sdk.robot.messages.SNContentMessage;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import lombok.extern.slf4j.Slf4j;

@SystemInstance
@Slf4j
@Marker("ROBOT")
public abstract class AbstractRobot implements Robot, InitializationBean {

    @Mount
    protected EventChannelImpl eventChannel;

    @Mount
    protected BeanContext context;

    @Mount
    protected Converters converters;

    @Mount
    protected IGUIBridge bridge;

    protected abstract void toLogin(long id, String password, String protocal) throws LoginFailedException;

    public abstract void close() throws Exception;

    public final void login(long id, String password, String protocal) {
        if (id == 0L) {
            log.warn("没有设置登录账号！");
            return;
        }
        if (password == null || password.isEmpty()) {
            password = bridge.prompt("SECURE", "请输入密码", "");
            if ("".equals(password)) {
                log.warn("没有输入密码！");
                return;
            }
        }
        log.info("准备登录");
        eventChannel.post(new LoginEvent("准备登录", LoginEvent.INFO));
        if (getStatus()) {
            log.error("重复的登录！");
            eventChannel.post(new LoginEvent("重复的登录", LoginEvent.ERROR));
            return;
        }
        log.info("登录中...");
        eventChannel.post(new LoginEvent("登录中...", LoginEvent.INFO));
        try {
            toLogin(id, password, protocal);
            log.debug("登录成功");
            eventChannel.post(new LoginEvent("登录成功", LoginEvent.SUCCESS));
        } catch (LoginFailedException t) {
            log.debug("登录失败", t);
            if (t instanceof WrongPasswordException) {
                log.error("登录失败！账号或者密码错误\n{}", t.getMessage());
            } else if (t instanceof ServerFailedException) {
                log.error("登录失败！服务器异常，请检查日志后重试！\n{}", t.getMessage());
            } else if (t instanceof InternalModelFailedException) {
                log.error("登录失败！内部异常，请检查日志后重试！\n{}", t.getMessage());
            } else {
                log.error("登录失败！意料之外的异常，请检查日志后重试！");
            }

            try {
                close();
            } catch (Exception e) {
                log.debug("关闭机器人出现异常", e);
            }
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
