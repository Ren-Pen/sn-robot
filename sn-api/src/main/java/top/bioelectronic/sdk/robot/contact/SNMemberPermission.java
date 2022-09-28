package top.bioelectronic.sdk.robot.contact;

import lombok.Getter;

public enum SNMemberPermission {

    MEMBER(0),

    /**
     * 管理员
     */
    ADMINISTRATOR(1),

    /**
     * 群主
     */
    OWNER(2);

    @Getter
    private final int level;

    SNMemberPermission(int level) {
        this.level = level;
    }
}
