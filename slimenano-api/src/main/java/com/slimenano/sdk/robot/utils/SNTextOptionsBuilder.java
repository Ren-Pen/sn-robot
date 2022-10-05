package com.slimenano.sdk.robot.utils;

import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.logger.Marker;
import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.SNMemberPermission;
import com.slimenano.sdk.robot.contact.user.SNFriend;
import com.slimenano.sdk.robot.contact.user.SNMember;
import com.slimenano.sdk.robot.contact.user.SNNormalMember;
import com.slimenano.sdk.robot.contact.user.SNStranger;
import com.slimenano.sdk.robot.events.messages.SNGroupMessageEvent;
import com.slimenano.sdk.robot.events.messages.SNMessageEvent;
import com.slimenano.sdk.robot.messages.SNMessage;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNAt;
import com.slimenano.sdk.robot.messages.content.SNText;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 选项构建器
 */
@Deprecated
@Slf4j
@Marker("会话构建器")
public class SNTextOptionsBuilder {

    private final Robot bot;
    private final String prefix;
    private final ConcurrentHashMap<String, OptionWrapper> optionMap = new ConcurrentHashMap<>();

    private static class OptionWrapper {
        public SNMessageChain chain;
        public Option option;
        public OptionWrapper(SNMessageChain chain, Option option) {
            this.chain = chain;
            this.option = option;
        }
    }

    @NoArgsConstructor

    public static class Option{
        @Setter
        boolean justMaster = false;
        @Setter
        SNMemberPermission permission = null;

        volatile HashMap<Class<? extends SNContact>, Set<Long>> allow_filters = new HashMap<>();

        public void noFilters(){
            allow_filters = null;
        }

        private Option addFilter(Class<? extends SNContact> clazz, long id){
            if (allow_filters == null) throw new NullPointerException("Option is NO_FILTER State");
            if (!allow_filters.containsKey(clazz)){
                allow_filters.put(clazz, new HashSet<>());
            }
            allow_filters.get(clazz).add(id);
            return this;
        }

        public Option addFriendFilter(long id) {
            return addFilter(SNFriend.class, id);
        }

        public Option addGroupFilter(long id) {
            return addFilter(SNGroup.class, id);
        }

        public Option addMemberTempFilter(long id) {
            return addFilter(SNMember.class, id);
        }

        public Option addStrangerFilter(long id) {
            return addFilter(SNStranger.class, id);
        }


    }

    @Setter
    private boolean atMe = false;

    @Setter
    private boolean trim = true;

    @Setter
    public long master = 0L;

    @Setter
    private boolean linkAllText = false;

    private volatile boolean insideCommandState = false;

    private OptionWrapper noMatcher = null;

    private SNMessageChain noPermission = null;

    public SNTextOptionsBuilder(Robot bot, String prefix) {
        log.warn("警告，插件使用了废弃的会话器，该会话器将在最新版本中被移除，请插件作者及时更换新的会话器！");
        this.bot = bot;
        this.prefix = prefix;
    }

    public void enableInsideCommand() {
        if (insideCommandState) return;
        insideCommandState = true;

    }

    public void setNoMatcher(SNMessageChain chain, Option option) {
        this.noMatcher = new OptionWrapper(chain, option);
    }
    public void setNoMatcher(String noMatcher, Option option) {
        setNoMatcher(new SNText(noMatcher).toChain(), option);
    }
    public void setNoMatcher(String noMatcher, Option option, Object... objects) {
        setNoMatcher(new SNText(noMatcher, objects).toChain(),option);
    }

    public void setNoPermission(SNMessageChain chain) {
        this.noPermission = chain;
    }
    public void setNoPermission(String noMatcher) {
        setNoPermission(new SNText(noMatcher).toChain());
    }
    public void setNoPermission(String noMatcher, Object... objects) {
        setNoPermission(new SNText(noMatcher, objects).toChain());
    }

    public SNTextOptionsBuilder addOption(String command, Option option, SNMessageChain chain) {
        if (option == null) throw new IllegalArgumentException("不可以没有筛选条件！");
        OptionWrapper optionWrapper = new OptionWrapper(chain, option);
        optionMap.put(command, optionWrapper);
        return this;
    }
    public SNTextOptionsBuilder addOption(String command, Option option, String result) {
        return addOption(command, option, new SNText(result).toChain());
    }
    public SNTextOptionsBuilder addOption(String command, Option option, String format, Object... objects) {
        return addOption(command, option, new SNText(format, objects).toChain());
    }





    private void sendMessage(SNMessageEvent e, OptionWrapper optionWrapper) {
        Option option = optionWrapper.option;
        SNContact target = e.getFrom();
        long mid = target.getId();
        if (e instanceof SNGroupMessageEvent){
            mid = ((SNGroupMessageEvent) e).getSender().getId();
        }
        if (option.justMaster && mid != master){
            return;
        }
        SNMessageChain chain = optionWrapper.chain;
        if (chain == null) return;
        if (target instanceof SNFriend) {
            if (option.allow_filters != null) {
                if (!option.allow_filters.containsKey(SNFriend.class)) return;
                if (!option.allow_filters.get(SNFriend.class).contains(target.getId()))
                    return;
            }
            bot.sendMessage((SNFriend) target, chain);
        } else if (target instanceof SNGroup) {
            if (option.allow_filters != null) {
                if (!option.allow_filters.containsKey(SNGroup.class)) return;
                if (!option.allow_filters.get(SNGroup.class).contains(target.getId()))
                    return;
                if (option.permission != null) {
                    if (option.permission.getLevel() > ((SNGroupMessageEvent) e).getSender().getPermission().getLevel()) {
                        if (noPermission != null)
                            bot.sendMessage((SNGroup) target, noPermission);
                        return;
                    }
                }
            }
            bot.sendMessage((SNGroup) target, chain);
        } else if (target instanceof SNNormalMember) {
            if (option.allow_filters != null) {
                if (!option.allow_filters.containsKey(SNMember.class)) return;
                if (!option.allow_filters.get(SNMember.class).contains(target.getId()))
                    return;
            }
            bot.sendMessage((SNNormalMember) target, chain);
        } else if (target instanceof SNStranger) {
            if (option.allow_filters != null) {
                if (!option.allow_filters.containsKey(SNStranger.class)) return;
                if (!option.allow_filters.get(SNStranger.class).contains(target.getId()))
                    return;
            }
            bot.sendMessage((SNStranger) target, chain);
        } else {
            throw new IllegalArgumentException("target cannot send message!");
        }


    }

    public void run(SNMessageEvent e) {

        SNMessageChain chain = e.getPayload();
        if (atMe && !chain.contains(new SNAt(bot.getBotId()))) {
            return;
        }

        SNMessage[] texts = chain.get(SNText.class);
        if (texts.length == 0) return;
        String command = "";
        if (linkAllText) {
            StringBuilder preCommand = new StringBuilder();
            for (SNMessage text : texts) {
                String content = ((SNText) text).getContent();
                if (trim) content = content.trim();
                preCommand.append(content);
            }
            command = preCommand.toString();
        } else {
            command = ((SNText) texts[0]).getContent();
            if (trim) command = command.trim();
        }

        if (!command.startsWith(prefix)) return;

        command = command.substring(prefix.length());
        log.debug("{} 接收到会话 {}", prefix, command);
        sendMessage(e, optionMap.getOrDefault(command, noMatcher));


    }


}
