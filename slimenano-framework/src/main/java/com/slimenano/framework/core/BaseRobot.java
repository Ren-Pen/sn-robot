package com.slimenano.framework.core;

import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.user.*;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNImage;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 基础机器人对象
 */
public abstract class BaseRobot extends AbstractRobot {


    @Override
    public void test()
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public long getBotId()
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNMessageSource sendMessage(SNFriend friend, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNMessageSource sendMessage(SNGroup group, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNMessageSource sendMessage(SNStranger stranger, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNMessageSource sendMessage(SNNormalMember member, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNFriend getFriend(long friendId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNGroup getGroup(long groupId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNMember getGroupMember(SNGroup group, long memberId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNStranger getStranger(long strangerId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public List<SNFriend> getFriendList()
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public List<SNGroup> getGroupsList()
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public List<SNMember> getGroupMembers(SNGroup group)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNImage uploadImg(File file)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNImage uploadImg(URL url, boolean forceUpdate)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public SNImage uploadImg(URL url)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public void nudge(SNUser target, SNContact sendTo)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public boolean recall(SNMessageSource source)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public boolean mute(SNMember member, int durationSeconds)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public boolean unmute(SNNormalMember member)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }

    @Override
    public boolean kick(SNNormalMember member, String message, boolean block)
            throws UnsupportedRobotOperationException, NoOperationPermissionException {
        throw new UnsupportedRobotOperationException("当前核心未实现操作");
    }
}
