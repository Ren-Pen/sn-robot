package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;
import top.bioelectronic.sdk.robot.exceptions.NoImplementException;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import lombok.Getter;

@Getter
public class SNAnonymousMemberImpl extends SNMemberImpl implements SNAnonymousMember {

    private final String anonymousId;

    public SNAnonymousMemberImpl(long id, SNUserProfile profile, SNGroup group, String specialTitle, String nameCard, SNMemberPermission permission, String anonymousId) {
        super(id, profile, group, specialTitle, nameCard, permission);
        this.anonymousId = anonymousId;
    }

    @Override
    public SNMessageSource sendMessage(Robot robot, SNMessageChain chain) {
        throw new NoImplementException("不能向匿名群成员发送临时会话！");
    }
}
