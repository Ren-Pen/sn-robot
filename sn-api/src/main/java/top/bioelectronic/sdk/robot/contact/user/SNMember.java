package top.bioelectronic.sdk.robot.contact.user;

import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.SNMemberPermission;

public interface SNMember extends SNUser {

    String getSpecialTitle();

    String getNameCard();

    SNMemberPermission getPermission();

    SNGroup getGroup();


}
