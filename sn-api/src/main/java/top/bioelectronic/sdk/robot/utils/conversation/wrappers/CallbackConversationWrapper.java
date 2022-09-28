package top.bioelectronic.sdk.robot.utils.conversation.wrappers;

import top.bioelectronic.sdk.robot.contact.SNContact;
import top.bioelectronic.sdk.robot.events.messages.SNMessageEvent;
import top.bioelectronic.sdk.robot.messages.SNMessage;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.content.SNText;
import top.bioelectronic.sdk.robot.utils.conversation.ConversationWrapper;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CallbackConversationWrapper extends ConversationWrapper {

    @Getter
    private final String prefix;

    private final HashMap<Pattern, Callback> cmds = new HashMap<>();

    @Getter
    private Advice preExec = null;

    @Getter
    private Advice postExec = null;
    @Getter
    private boolean trim = true;

    public CallbackConversationWrapper(long target, @NonNull String prefix) {
        super(target);
        this.prefix = prefix;
    }

    public CallbackConversationWrapper setPreExec(Advice preExec) {
        this.preExec = preExec;
        return this;
    }

    public CallbackConversationWrapper setPostExec(Advice postExec) {
        this.postExec = postExec;
        return this;
    }

    public CallbackConversationWrapper addCommand(String cmdRegex, Callback callback) {

        cmds.put(Pattern.compile(cmdRegex), callback);
        return this;

    }

    @Override
    protected int customTest(SNMessageEvent event, SNMessageChain chain) {
        String cmd = dealChain(event.getPayload());
        if (cmd == null) return NONE;
        if (!cmd.startsWith(prefix)) return NONE;
        String cmd_s = cmd.substring(prefix.length());
        List<Pattern> collect = cmds.keySet().stream().filter(pattern -> pattern.matcher(cmd_s).matches()).collect(Collectors.toList());
        if (collect.isEmpty()) return UNKWON_COMMAND;

        collect.forEach(pattern -> {

            Matcher matcher = pattern.matcher(cmd_s);
            matcher.matches();
            int size = matcher.groupCount();
            String[] args = new String[size + 1];
            args[0] = cmd_s;
            for (int i = 1; i <= size; i++) {
                args[i] = matcher.group(i);
            }
            if (preExec != null) {
                if (!preExec.exec(event.getFrom(), chain, args)) {
                    chain.add(new SNText("指令：{} 预处理失败！", cmd_s));
                    return;
                }
            }

            if (!cmds.get(pattern).exec(event.getFrom(), chain, args)) {
                chain.add(new SNText("指令：{}，执行失败！", cmd_s));
                return;
            }

            if (postExec != null) {
                if (!postExec.exec(event.getFrom(), chain, args)) {
                    chain.add(new SNText("指令：{} 后处理失败！", cmd_s));
                }
            }

        });

        return OK;
    }

    private String dealChain(SNMessageChain chain) {

        SNMessage[] texts = chain.get(SNText.class);
        if (texts.length != 1) return null;
        String content = ((SNText) texts[0]).getContent();
        if (trim) content = content.trim();
        return content;

    }

    public CallbackConversationWrapper setTrim(boolean trim) {
        this.trim = trim;
        return this;
    }

    @FunctionalInterface
    public interface Callback {
        boolean exec(SNContact contact, SNMessageChain chain, String... groups);
    }

    @FunctionalInterface
    public interface Advice {
        boolean exec(SNContact contact, SNMessageChain chain, String... groups);
    }

}
