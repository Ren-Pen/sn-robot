package com.slimenano.sdk.robot.utils.conversation.wrappers;

import com.slimenano.sdk.robot.events.messages.SNMessageEvent;
import com.slimenano.sdk.robot.messages.SNMessage;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNText;
import lombok.Getter;
import lombok.NonNull;
import com.slimenano.sdk.robot.utils.conversation.ConversationWrapper;

import java.util.HashMap;

/**
 * 纯文本会话条件判断
 * 拼接所有文本
 * 删除首尾空格（片段，完整）
 * 每种命令包含一种回复
 */
public class TextConversationWrapper extends ConversationWrapper {

    @Getter
    private final String prefix;

    @Getter
    private boolean linkAll = false;

    @Getter
    private boolean trim = true;

    @Getter
    private boolean trimPiece = false;

    private final HashMap<String, SNMessageChain> cmds = new HashMap<>();

    public TextConversationWrapper(long target, @NonNull String prefix) {
        super(target);
        this.prefix = prefix;
    }


    public TextConversationWrapper addCommand(String cmd, SNMessageChain reply){

        cmds.put(cmd, reply);
        return this;

    }
    public TextConversationWrapper addCommand(String cmd, String msg){
        return addCommand(cmd, new SNText(msg).toChain());

    }
    public TextConversationWrapper addCommand(String cmd, String msg, Object... param){
        return addCommand(cmd, new SNText(msg, param).toChain());

    }



    /**
     * 处理串
     *
     * @param chain
     *
     * @return
     */
    protected String dealChain(SNMessageChain chain) {
        if (chain.contains(SNText.class)) {
            SNMessage[] texts = chain.get(SNText.class);
            StringBuilder sb = new StringBuilder();
            if (linkAll) {
                for (int i = 0; i < texts.length; i++) {
                    SNText text = (SNText) texts[i];
                    if (trim && trimPiece) {
                        sb.append(text.getContent().trim());
                    } else {
                        sb.append(text.getContent());
                    }
                }
            } else {
                sb.append(((SNText) texts[0]).getContent());
            }
            String result = sb.toString();
            if (trim) result = result.trim();
            return result;

        }
        return null;
    }

    @Override
    public int customTest(SNMessageEvent event, SNMessageChain chain) {

        String cmd = dealChain(event.getPayload());
        if (cmd == null) return NONE;
        if (!cmd.startsWith(prefix)) return NONE;
        String cmd_s = cmd.substring(prefix.length());
        if (!cmds.containsKey(cmd_s)) return UNKWON_COMMAND;
        chain.addAll(cmds.get(cmd_s));
        return OK;

    }

    public TextConversationWrapper setLinkAll(boolean linkAll) {
        this.linkAll = linkAll;
        return this;
    }
    public TextConversationWrapper setTrim(boolean trim) {
        this.trim = trim;
        return this;
    }
    public TextConversationWrapper setTrimPiece(boolean trimPiece) {
        this.trimPiece = trimPiece;
        return this;
    }




}
