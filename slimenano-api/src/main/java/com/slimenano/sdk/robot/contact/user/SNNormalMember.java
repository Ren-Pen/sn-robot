package com.slimenano.sdk.robot.contact.user;

public interface SNNormalMember extends SNMember {

    int getMuteTimeRemaining();
    int getLastSpeakTimestamp();
    int getJoinTimestamp();
    boolean isMute();

}
