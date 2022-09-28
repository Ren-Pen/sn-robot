package top.bioelectronic.sdk.robot.utils.conversation;

import top.bioelectronic.sdk.access.Access;
import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.event.annotations.EventListener;
import top.bioelectronic.sdk.event.annotations.GenericEventType;
import top.bioelectronic.sdk.event.annotations.OnSuperSubscribe;
import top.bioelectronic.sdk.event.annotations.Subscribe;
import top.bioelectronic.sdk.framework.InitializationBean;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.plugin.PluginInformation;
import top.bioelectronic.sdk.robot.events.messages.SNGroupMessageEvent;
import top.bioelectronic.sdk.robot.events.messages.SNMessageEvent;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.content.SNAt;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import lombok.Getter;
import lombok.Setter;
import top.bioelectronic.sdk.access.AccessManager;

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
public abstract class Conversation<T extends SNMessageEvent> implements InitializationBean {

    @Mount
    AccessManager accessManager;

    @Mount
    PluginInformation information;

    @Getter
    @Setter
    private boolean enable = true;

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


    protected abstract void onLoading() throws Exception;

    @Override
    public final void onLoad() throws Exception {
        if (!accessManager.hasAccess(information, Access.BEHAVIOR_GET_BOT_ID)){
            if (!accessManager.useAccess(information, Access.BEHAVIOR_GET_BOT_ID)) {
                throw new RuntimeException("没有足够的权限，会话器无法启动！");
            }
        }
        onLoading();
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
