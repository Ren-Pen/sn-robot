package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;
import lombok.Getter;

@Getter
public abstract class SNMemberImpl extends SNUserImpl implements SNMember {

    private final SNGroup group;
    private final String specialTitle;
    private final String nameCard;
    private final SNMemberPermission permission;

    protected SNMemberImpl(long id, SNUserProfile profile, SNGroup group, String specialTitle, String nameCard, SNMemberPermission permission) {
        super(id, profile);
        this.group = group;
        this.specialTitle = specialTitle;
        this.nameCard = nameCard;
        this.permission = permission;
    }


}
