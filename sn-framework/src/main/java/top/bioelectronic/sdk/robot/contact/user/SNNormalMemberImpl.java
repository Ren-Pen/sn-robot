package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import lombok.Getter;

@Getter
public class SNNormalMemberImpl extends SNMemberImpl implements SNNormalMember {
    private final int muteTimeRemaining;
    private final int lastSpeakTimestamp;
    private final int joinTimestamp;

    public SNNormalMemberImpl(long id, SNUserProfile profile, SNGroup group, String specialTitle, String nameCard, SNMemberPermission permission, int joinTimestamp, int lastSpeakTimestamp, int muteTimeRemaining) {
        super(id, profile, group, specialTitle, nameCard, permission);
        this.joinTimestamp = joinTimestamp;
        this.lastSpeakTimestamp = lastSpeakTimestamp;
        this.muteTimeRemaining = muteTimeRemaining;
    }

    @Override
    public SNMessageSource sendMessage(Robot robot, SNMessageChain chain) {
        return robot.sendMessage(this, chain);
    }

}
