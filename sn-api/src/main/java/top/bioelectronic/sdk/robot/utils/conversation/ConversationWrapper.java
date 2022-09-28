package top.bioelectronic.sdk.robot.utils.conversation;

import top.bioelectronic.sdk.common.Nullable;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;
import top.bioelectronic.sdk.robot.contact.user.SNMember;
import top.bioelectronic.sdk.robot.events.messages.SNGroupMessageEvent;
import top.bioelectronic.sdk.robot.events.messages.SNMessageEvent;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.content.SNAt;
import top.bioelectronic.sdk.robot.messages.content.SNText;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 会话条件判断
 * 会话的目标 id
 * 可处理的权限 （仅群租）
 * 是否需要At机器人（仅群组）
 */
public abstract class ConversationWrapper {

    @Data
    static class ConversationWrapperResult{
        private int code = NONE;
        private SNMessageChain chain = null;
    }

    public static final int NONE = -1;
    public static final int OK = 0;
    public static final int NO_PERMISSION = 1;
    public static final int UNKWON_COMMAND = 2;

    @Getter
    private final long target;

    @Getter
    @Nullable
    private SNMessageChain noPermission = null;

    @Getter
    @Nullable
    private SNMessageChain noMatcher = null;

    @Getter
    private boolean atMe = false;

    @Getter
    private boolean atTarget = false;

    /**
     * 不需要手动设置，对话器会自动配置
     */
    @Setter(AccessLevel.PACKAGE)
    private long botId = 0L;

    @Getter
    private SNMemberPermission minPermission = SNMemberPermission.MEMBER;

    @Getter
    private SNMemberPermission maxPermission = SNMemberPermission.OWNER;

    /**
     * 仅机器人所有者生效，仅用于群组消息
     */
    @Getter
    private Long master = null;

    public ConversationWrapper(long target) {
        this.target = target;
    }

    public final ConversationWrapper setAtMe(boolean atMe) {
        this.atMe = atMe;
        return this;
    }
    public final ConversationWrapper setAtTarget(boolean atTarget) {
        this.atTarget = atTarget;
        return this;
    }
    public final ConversationWrapper setMaster(long master){
        this.master = master;
        return this;
    }

    public final ConversationWrapper setMinPermission(SNMemberPermission minPermission) {
        this.minPermission = minPermission;
        return this;
    }
    public final ConversationWrapper setMaxPermission(SNMemberPermission maxPermission) {
        this.maxPermission = maxPermission;
        return this;
    }

    public final ConversationWrapper setNoPermission(SNMessageChain chain) {
        this.noPermission = chain;
        return this;
    }
    public final ConversationWrapper setNoPermission(String chain) {
        if (chain == null || chain.isEmpty()){
            return setNoMatcher((SNMessageChain) null);
        }
        return setNoPermission(new SNText(chain).toChain());
    }
    public final ConversationWrapper setNoPermission(String chain, Object... param) {
        if (chain == null || chain.isEmpty()){
            return setNoMatcher((SNMessageChain) null);
        }
        return setNoPermission(new SNText(chain, param).toChain());
    }

    public final ConversationWrapper setNoMatcher(SNMessageChain chain) {
        this.noMatcher = chain;
        return this;
    }
    public final ConversationWrapper setNoMatcher(String chain) {
        if (chain == null || chain.isEmpty()){
            return setNoMatcher((SNMessageChain) null);
        }
        return setNoMatcher(new SNText(chain).toChain());
    }
    public final ConversationWrapper setNoMatcher(String chain, Object... param) {
        if (chain == null || chain.isEmpty()){
            return setNoMatcher((SNMessageChain) null);
        }
        return setNoMatcher(new SNText(chain, param).toChain());
    }

    protected abstract int customTest(SNMessageEvent event, SNMessageChain result);

    /**
     * 回复消息即将发送前
     *
     * @param event 来源消息
     * @param chain 即将发送消息
     */
    protected void preSend(SNMessageEvent event, SNMessageChain chain) {

    }

    /**
     * 回复消息发送后
     *
     * @param event  来源消息
     * @param chain  已经发送消息
     * @param source 发送后的消息源
     */
    protected void postSend(SNMessageEvent event, SNMessageChain chain, SNMessageSource source) {

    }

    /**
     * 执行判断
     *
     * @return
     */
    public ConversationWrapperResult test(SNMessageEvent event) {
        ConversationWrapperResult result = new ConversationWrapperResult();
        if (event.getFrom().getId() != target) return result;
        if (atMe && !event.getPayload().contains(new SNAt(botId))) return result;

        if (event instanceof SNGroupMessageEvent) {
            SNMember sender = ((SNGroupMessageEvent) event).getSender();
            if (getMaster() == null){
                SNMemberPermission permission = sender.getPermission();
                if (permission.getLevel() > maxPermission.getLevel()
                        || permission.getLevel() < minPermission.getLevel()) {
                    result.setCode(NO_PERMISSION);
                    return result;
                }
            }else{
                if (sender.getId() != getMaster())
                    return result;
            }

        }

        SNMessageChain chain = new SNMessageChain();
        int i = customTest(event, chain);
        result.setCode(i);
        if (i == OK) result.setChain(chain);

        return result;
    }

}
