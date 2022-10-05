package com.demo;

import com.slimenano.sdk.framework.annotations.Mount;
import com.slimenano.sdk.robot.events.messages.SNGroupMessageEvent;
import com.slimenano.sdk.robot.messages.content.SNText;
import com.slimenano.sdk.robot.utils.conversation.Conversation;
import com.slimenano.sdk.robot.utils.conversation.wrappers.CallbackConversationWrapper;

import java.util.Arrays;

public class MyConversation2 extends Conversation<SNGroupMessageEvent> {

    @Mount
    DemoPlugin plugin;

    @Mount
    MyConfiguration config;

    @Override
    public void onLoading() throws Exception {

        setEnable(config.isEnable());

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
