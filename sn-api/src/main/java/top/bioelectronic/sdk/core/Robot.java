package top.bioelectronic.sdk.core;

import top.bioelectronic.sdk.common.Nullable;
import top.bioelectronic.sdk.robot.contact.SNGroup;
import top.bioelectronic.sdk.robot.contact.user.SNFriend;
import top.bioelectronic.sdk.robot.contact.user.SNMember;
import top.bioelectronic.sdk.robot.contact.user.SNNormalMember;
import top.bioelectronic.sdk.robot.contact.user.SNStranger;
import top.bioelectronic.sdk.robot.messages.SNMessageChain;
import top.bioelectronic.sdk.robot.messages.content.SNImage;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface Robot {

    String base_version = "alpha-v1.0.0.021";

    void test();

    SNMessageSource sendMessage(SNFriend friend, SNMessageChain chain);

    SNMessageSource sendMessage(SNGroup group, SNMessageChain chain);

    SNMessageSource sendMessage(SNStranger stranger, SNMessageChain chain);

    SNMessageSource sendMessage(SNNormalMember member, SNMessageChain chain);

    SNFriend getFriend(long friendId);

    SNGroup getGroup(long groupId);

    SNMember getGroupMember(SNGroup group, long memberId);

    SNStranger getStranger(long strangerId);

    List<SNFriend> getFriendList();

    List<SNGroup> getGroupsList();

    @Nullable
    List<SNMember> getGroupMembers(SNGroup group);

    long getBotId();

    SNImage uploadImg(File file) throws IOException;

    SNImage uploadImg(URL url, boolean forceUpdate) throws IOException;

    SNImage uploadImg(URL url) throws IOException;

    boolean recall(SNMessageSource source);

    String getCoreType();
}
