package com.slimenano.sdk.robot.utils.conversation;

import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.event.annotations.EventListener;
import com.slimenano.sdk.event.annotations.GenericEventType;
import com.slimenano.sdk.event.annotations.OnSuperSubscribe;
import com.slimenano.sdk.event.annotations.Subscribe;
import com.slimenano.sdk.framework.InitializationBean;
import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.plugin.PluginInformation;
import com.slimenano.sdk.robot.events.BotLinkStateChangeEvent;
import com.slimenano.sdk.robot.events.messages.SNGroupMessageEvent;
import com.slimenano.sdk.robot.events.messages.SNMessageEvent;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNAt;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;
import lombok.Getter;
import lombok.Setter;
import com.slimenano.sdk.access.AccessManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * Mirai选项对话器
 * <p>
 * 实现该类即可获得对话器，通过addWrapper添加对话
 *
 * @param <T> 处理的事件类型
 */
@EventListener
@OnSuperSubscribe(superClazz = Conversation.class)
@Slf4j
@Marker("选项会话器")
public abstract class Conversation<T extends SNMessageEvent> implements InitializationBean {

    @Mount
    AccessManager accessManager;

    @Mount
    PluginInformation information;

    @Getter
    @Setter
    private volatile boolean enable = true;

    private volatile boolean loaded = false;

    /**
     * 保存所有的wrapper
     */
    private final ArrayList<ConversationWrapper> wrappers = new ArrayList<>();

    @Mount
    protected Robot robot;

    protected final void addWrapper(ConversationWrapper... wrappers) {
        for (ConversationWrapper wrapper : wrappers) {
            addWrapper(wrapper);
        }
    }

    protected final void addWrapper(ConversationWrapper wrapper) {
        wrapper.setBotId(robot.getBotId());
        wrappers.add(wrapper);

    }

    protected final void removeWrapper(ConversationWrapper wrapper) {

        wrappers.remove(wrapper);


    }

    private void load(){
        if (!loaded) {
            synchronized (this) {
                if (!loaded) {
                    loaded = true;
                    try {
                        onLoading();
                    } catch (Exception e) {
                        log.error("会话器加载时出现了错误，会话器功能将部分不可用！", e);
                    }
                }
            }
        }
    }

    @Subscribe
    public final void onBotStateChange(BotLinkStateChangeEvent event){
        if (event.getState() == BotLinkStateChangeEvent.ONLINE){
            load();
        }
    }

    protected abstract void onLoading() throws Exception;

    @Override
    public final void onLoad() throws Exception {
        if (!accessManager.hasAccess(information, Permission.BEHAVIOR_GET_BOT_ID)){
            if (!accessManager.useAccess(information, Permission.BEHAVIOR_GET_BOT_ID)) {
                throw new RuntimeException("没有足够的权限，会话器无法启动！");
            }
        }
        if (!robot.isClose()){
            load();
        }

    }

    /**
     * 这里做销毁工作
     *
     * @throws Exception
     */
    @Override
    public final void onDestroy() throws Exception {
        wrappers.clear();

    }

    @Subscribe
    @GenericEventType
    public final void onMessageEvent(T event) {
        if (!enable) return;
        long id = event.getFrom().getId();
        if (event instanceof SNGroupMessageEvent) {
            id = ((SNGroupMessageEvent) event).getSender().getId();
        }

        final long finalId = id;
        wrappers.forEach(wrapper -> {
            SNMessageChain waitingSend = null;

            ConversationWrapper.ConversationWrapperResult result = wrapper.test(event);
            switch (result.getCode()) {
                case ConversationWrapper.OK:
                    waitingSend = result.getChain();
                    break;
                case ConversationWrapper.NO_PERMISSION:
                    waitingSend = wrapper.getNoPermission();
                    break;
                case ConversationWrapper.UNKWON_COMMAND:
                    waitingSend = wrapper.getNoMatcher();
                    break;
            }

            if (waitingSend != null) {
                if (wrapper.isAtTarget()) {
                    waitingSend.add(0, new SNAt(finalId));
                }
                wrapper.preSend(event, waitingSend);
                SNMessageSource source = event.getFrom().sendMessage(robot, waitingSend);
                wrapper.postSend(event, waitingSend, source);
            }

        });

    }


}
