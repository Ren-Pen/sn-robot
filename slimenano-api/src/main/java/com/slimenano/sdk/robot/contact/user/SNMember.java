package com.slimenano.sdk.robot.contact.user;

import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.SNMemberPermission;

public interface SNMember extends SNUser {

    String getSpecialTitle();

    String getNameCard();

    SNMemberPermission getPermission();

    SNGroup getGroup();


}
