package com.demo;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.framework.annotations.Mount;
import top.bioelectronic.sdk.robot.events.messages.SNGroupMessageEvent;
import top.bioelectronic.sdk.robot.utils.conversation.Conversation;
import top.bioelectronic.sdk.robot.utils.conversation.wrappers.TextConversationWrapper;
import top.bioelectronic.sdk.plugin.BasePlugin;

public class MyConversation extends Conversation<SNGroupMessageEvent> {

    @Mount
    DemoPlugin plugin;

    @Mount
    MyConfiguration config;

    @Override
    public void onLoading() throws Exception {

        setEnable(config.isEnable());

        TextConversationWrapper wrapper = new TextConversationWrapper(config.getTarget(), config.getPrefix())
                .addCommand(
                        "指令列表",
                        "示例插件指令列表：\n"
                                + config.getPrefix() + "指令列表\n"
                                + config.getPrefix() + "运行环境\n"
                                + config.getPrefix() + "五角星\n"
                )
                .addCommand(
                        "运行环境",
                        "当前核心版本：{}\nSDK版本：{}\n图形界面：{} 版本：{}",
                        Robot.base_version, BasePlugin.SDK_VERSION, plugin.getBridge().getName(), plugin.getBridge().getVersion()
                )
                .addCommand(
                        "五角星",
                        "            *\n" +
                                "          *   *\n" +
                                "        *       *  \n" +
                                "    *               *\n" +
                                "       *    *    *\n" +
                                "     *   *     *   *\n" +
                                "   *                 *"
                );

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
