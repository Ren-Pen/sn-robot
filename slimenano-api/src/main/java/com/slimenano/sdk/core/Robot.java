package com.slimenano.sdk.core;

import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.common.Nullable;
import com.slimenano.sdk.framework.annotations.AccessControl;
import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.user.*;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNImage;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface Robot {

    String base_version = "alpha-v1.0.0.021";

    boolean isClose();

    /**
     * 测试用方法，没有实际效果
     */
    @AccessControl(require = {Permission.TEST})
    void test();

    /**
     * 获取BOT ID
     *
     * @return
     */
    @AccessControl(require = Permission.BEHAVIOR_GET_BOT_ID)
    long getBotId();

    /**
     * 发送消息到好友
     *
     * @param friend
     * @param chain
     *
     * @return
     */
    @AccessControl(require = {Permission.SEND_FRIEND})
    SNMessageSource sendMessage(SNFriend friend, SNMessageChain chain);

    /**
     * 发送消息到群组
     *
     * @param group
     * @param chain
     *
     * @return
     */
    @AccessControl(require = {Permission.SEND_GROUP})
    SNMessageSource sendMessage(SNGroup group, SNMessageChain chain);

    /**
     * 发送消息到陌生人
     *
     * @param stranger
     * @param chain
     *
     * @return
     */
    @AccessControl(require = {Permission.SEND_STRANGER})
    SNMessageSource sendMessage(SNStranger stranger, SNMessageChain chain);

    /**
     * 发送消息到群成员
     *
     * @param member
     * @param chain
     *
     * @return
     */
    @AccessControl(require = {Permission.SEND_GROUP_MEMBER})
    SNMessageSource sendMessage(SNNormalMember member, SNMessageChain chain);

    /**
     * 根据id获取好友
     *
     * @param friendId
     *
     * @return
     */
    @AccessControl(require = {Permission.GET_FRIEND})
    SNFriend getFriend(long friendId);

    /**
     * 根据id获取群组
     *
     * @param groupId
     *
     * @return
     */
    @AccessControl(require = {Permission.GET_GROUP})
    SNGroup getGroup(long groupId);

    /**
     * 根据群组和id获取群成员
     *
     * @param group
     * @param memberId
     *
     * @return
     */
    @AccessControl(require = {Permission.GET_GROUP_MEMBER})
    SNMember getGroupMember(SNGroup group, long memberId);

    /**
     * 根据id获取陌生人
     *
     * @param strangerId
     *
     * @return
     */
    @AccessControl(require = {Permission.GET_STRANGER})
    SNStranger getStranger(long strangerId);

    /**
     * 获取好友列表
     *
     * @return
     */
    @AccessControl(require = Permission.GET_FRIENDS)
    List<SNFriend> getFriendList();

    /**
     * 获取群列表
     *
     * @return
     */
    @AccessControl(require = Permission.GET_GROUPS)
    List<SNGroup> getGroupsList();

    /**
     * 获取群成员列表
     *
     * @param group
     *
     * @return
     */
    @Nullable
    @AccessControl(require = Permission.GET_GROUP_MEMBERS)
    List<SNMember> getGroupMembers(SNGroup group);

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param file
     *
     * @return
     *
     * @throws IOException
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD)
    SNImage uploadImg(File file) throws IOException;

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param url
     * @param forceUpdate 强制服务器刷新图片文件，即使md5相同
     *
     * @return
     *
     * @throws IOException
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD)
    SNImage uploadImg(URL url, boolean forceUpdate) throws IOException;

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param url
     *
     * @return
     *
     * @throws IOException
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD)
    SNImage uploadImg(URL url) throws IOException;

    /**
     * 戳一戳
     *
     * @param target 被戳的目标
     * @param sendTo 发送到哪里
     */
    @AccessControl(require = Permission.BEHAVIOR_NUDGE)
    void nudge(SNUser target, SNContact sendTo);

    /**
     * 撤回消息
     *
     * @param source
     *
     * @return
     */
    @AccessControl(require = Permission.BEHAVIOR_RECALL)
    boolean recall(SNMessageSource source);

    /**
     * 禁言群成员
     *
     * @param member
     * @param durationSeconds 禁言秒数
     *
     * @return
     */
    @AccessControl(require = Permission.BEHAVIOR_MUTE_MEMBER)
    boolean mute(SNMember member, int durationSeconds);

    /**
     * 解除禁言
     * @param member
     * @return
     */
    @AccessControl(require = Permission.BEHAVIOR_MUTE_MEMBER)
    boolean unmute(SNNormalMember member);

    /**
     * 踢出成员
     * @param member
     * @param message 踢出消息
     * @param block 是否拉黑
     * @return
     */
    @AccessControl(require = Permission.BEHAVIOR_KICK_MEMBER)
    boolean kick(SNNormalMember member, String message, boolean block);

    /**
     * 获取核心类别
     *
     * @return
     */
    String getCoreType();
}
