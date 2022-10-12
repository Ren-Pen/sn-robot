package com.slimenano.sdk.core;

import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.common.Nullable;
import com.slimenano.sdk.framework.annotations.AccessControl;
import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.contact.SNGroup;
import com.slimenano.sdk.robot.contact.user.*;
import com.slimenano.sdk.robot.exception.file.OverFileSizeMaxException;
import com.slimenano.sdk.robot.exception.message.MessageTooLargeException;
import com.slimenano.sdk.robot.exception.permission.BotNoPermissionException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedStatusException;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNAudio;
import com.slimenano.sdk.robot.messages.content.SNImage;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 机器人接口
 * <p>
 * 注意：当 {@link AccessControl} 的 status 为 true 时，代表该方法只能在 {@code getStatus()=true} 时调用
 * <br/>否则就会抛出 {@link UnsupportedStatusException} 异常
 */
public interface Robot {

    String base_version = "alpha-v1.0.0.021";

    /**
     * 机器人实例是否关闭，注意这个代表的是内部机器人实例是否已经close，不能用来检测是否在线
     *
     * @return
     */
    boolean isClose();

    /**
     * 测试用方法，没有实际效果
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     */
    @AccessControl(require = {Permission.TEST})
    void test()
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 获取BOT ID
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     */
    @AccessControl(require = Permission.BEHAVIOR_GET_BOT_ID, status = true)
    long getBotId()
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 发送消息到好友
     *
     * @param friend
     * @param chain
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    @AccessControl(require = {Permission.SEND_FRIEND}, status = true)
    SNMessageSource sendMessage(SNFriend friend, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

    /**
     * 发送消息到群组
     *
     * @param group
     * @param chain
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    @AccessControl(require = {Permission.SEND_GROUP}, status = true)
    SNMessageSource sendMessage(SNGroup group, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

    /**
     * 发送消息到陌生人
     *
     * @param stranger
     * @param chain
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    @AccessControl(require = {Permission.SEND_STRANGER}, status = true)
    SNMessageSource sendMessage(SNStranger stranger, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

    /**
     * 发送消息到群成员
     *
     * @param member
     * @param chain
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    @AccessControl(require = {Permission.SEND_GROUP_MEMBER}, status = true)
    SNMessageSource sendMessage(SNNormalMember member, SNMessageChain chain)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

    /**
     * 根据id获取好友
     *
     * @param friendId
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = {Permission.GET_FRIEND}, status = true)
    SNFriend getFriend(long friendId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 根据id获取群组
     *
     * @param groupId
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = {Permission.GET_GROUP}, status = true)
    SNGroup getGroup(long groupId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 根据群组和id获取群成员
     *
     * @param group
     * @param memberId
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = {Permission.GET_GROUP_MEMBER}, status = true)
    SNMember getGroupMember(SNGroup group, long memberId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 根据id获取陌生人
     *
     * @param strangerId
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = {Permission.GET_STRANGER}, status = true)
    SNStranger getStranger(long strangerId)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 获取好友列表
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = Permission.GET_FRIENDS, status = true)
    List<SNFriend> getFriendList()
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 获取群列表
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = Permission.GET_GROUPS, status = true)
    List<SNGroup> getGroupsList()
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 获取群成员列表
     *
     * @param group
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @Nullable
    @AccessControl(require = Permission.GET_GROUP_MEMBERS, status = true)
    List<SNMember> getGroupMembers(SNGroup group)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param file
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws OverFileSizeMaxException           上传文件太大导致异常
     * @throws IOException                        上传文件失败时抛出
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD, status = true)
    SNImage uploadImg(SNContact contact, File file)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param url
     * @param forceUpdate 强制服务器刷新图片文件，即使md5相同
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws OverFileSizeMaxException           上传文件太大导致异常
     * @throws IOException                        下载文件失败或上传文件失败时抛出
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD, status = true)
    SNImage uploadImg(SNContact contact, URL url, boolean forceUpdate)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    /**
     * 上传图片到服务器，获取图片对象
     *
     * @param url
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws OverFileSizeMaxException           上传文件太大导致异常
     * @throws IOException                        下载文件失败或上传文件失败时抛出
     */
    @AccessControl(require = Permission.BEHAVIOR_IMG_UPLOAD, status = true)
    SNImage uploadImg(SNContact contact, URL url)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    /**
     * 上传语音到服务器，获取语音对象
     *
     * @param file
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws OverFileSizeMaxException           上传文件太大导致异常
     * @throws IOException                        下载文件失败或上传文件失败时抛出
     */
    SNAudio uploadAudio(SNContact contact, File file) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    /**
     * 上传语音到服务器，获取语音对象
     *
     * @param url
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws OverFileSizeMaxException           上传文件太大导致异常
     * @throws IOException                        下载文件失败或上传文件失败时抛出
     */
    SNAudio uploadAudio(SNContact contact, URL url) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    /**
     * 戳一戳
     *
     * @param target 被戳的目标
     * @param sendTo 发送到哪里
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     */
    @AccessControl(require = Permission.BEHAVIOR_NUDGE, status = true)
    void nudge(SNUser target, SNContact sendTo)
            throws UnsupportedRobotOperationException, NoOperationPermissionException;

    /**
     * 撤回消息
     *
     * @param source
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws BotNoPermissionException           机器人没有权限进行该操作
     * @throws IllegalStateException              消息状态不能被撤回抛出（消息已经被撤回）
     * @
     */
    @AccessControl(require = Permission.BEHAVIOR_RECALL, status = true)
    void recall(SNMessageSource source)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, BotNoPermissionException;

    /**
     * 禁言群成员
     *
     * @param member
     * @param durationSeconds 禁言秒数
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws BotNoPermissionException           机器人没有权限进行该操作
     * @throws IllegalStateException              时间超过范围
     */
    @AccessControl(require = Permission.BEHAVIOR_MUTE_MEMBER, status = true)
    void mute(SNMember member, int durationSeconds)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, BotNoPermissionException, IllegalStateException;

    /**
     * 解除禁言
     *
     * @param member
     *
     * @return
     *
     * @throws NoOperationPermissionException      插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException, 机器人核心未实现该方法时抛出
     * @throws BotNoPermissionException            机器人没有权限进行该操作
     */
    @AccessControl(require = Permission.BEHAVIOR_MUTE_MEMBER, status = true)
    void unmute(SNNormalMember member)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, BotNoPermissionException;

    /**
     * 踢出成员
     *
     * @param member
     * @param message 踢出消息
     * @param block   是否拉黑
     *
     * @return
     *
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws BotNoPermissionException           机器人没有权限进行该操作
     */
    @AccessControl(require = Permission.BEHAVIOR_KICK_MEMBER, status = true)
    void kick(SNNormalMember member, String message, boolean block)
            throws UnsupportedRobotOperationException, NoOperationPermissionException, BotNoPermissionException;

    /**
     * 获取核心类别
     *
     * @return
     */
    String getCoreType();

    /**
     * 获取机器人在线状态
     *
     * @return
     */
    boolean getStatus();
}
