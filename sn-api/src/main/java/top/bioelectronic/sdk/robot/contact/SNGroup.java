package top.bioelectronic.sdk.robot.contact;

public interface SNGroup extends SNContact {


    String getName();
    String getAvatarUrl();
    SNMemberPermission getBotPermission();
    long getOwner();



}
