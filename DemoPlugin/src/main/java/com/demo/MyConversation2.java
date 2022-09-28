package com.demo;

import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.robot.events.messages.SNGroupMessageEvent;
import top.bioelectronic.sdk.robot.messages.content.SNText;
import top.bioelectronic.sdk.robot.utils.conversation.Conversation;
import top.bioelectronic.sdk.robot.utils.conversation.wrappers.CallbackConversationWrapper;

import java.util.Arrays;

public class MyConversation2 extends Conversation<SNGroupMessageEvent> {

    @Mount
    DemoPlugin plugin;

    @Mount
    MyConfiguration config;

    @Override
    public void onLoading() throws Exception {

        CallbackConversationWrapper wrapper = new CallbackConversationWrapper(config.getTarget(), config.getPrefix())
                .addCommand("(\\d{4})-(\\d{2}-(\\d\\d))", ((target, chain, args) -> {
                    chain.plus(new SNText("已执行，捕获列表：{}", Arrays.toString(args)));
                    return true;
                }));

        wrapper
                .setAtTarget(config.isAtTarget())
                .setAtMe(config.isAtBot())
                .setNoMatcher(config.getNoMatcher())
                .setNoPermission(config.getNoPermission())
                .setMaxPermission(config.getMaxPermission())
                .setMinPermission(config.getMinPermission());
        addWrapper(wrapper);
    }

}
