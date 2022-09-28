package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import lombok.Getter;

@Getter
public class SNFriendImpl extends SNUserImpl implements SNFriend {

    private final String nickname;
    private final String remark;

    public SNFriendImpl(long id, SNUserProfile profile, String nickname, String remark) {
        super(id, profile);
        this.nickname = nickname;
        this.remark = remark;
    }

    public SNFriendImpl(long id, String nickname, String remark) {
        super(id, null);
        this.nickname = nickname;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "[好友][" + getId() + "]" + "[" + (getRemark().isEmpty() ? getNickname() : getRemark()) + "]";
    }

    @Override
    public SNMessageSource sendMessage(Robot robot, SNMessageChain chain) {
        return robot.sendMessage(this, chain);
    }
}
