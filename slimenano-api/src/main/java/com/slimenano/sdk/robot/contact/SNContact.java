package com.slimenano.sdk.robot.contact;


import com.slimenano.sdk.access.Permission;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.framework.annotations.AccessControl;
import com.slimenano.sdk.robot.exception.file.OverFileSizeMaxException;
import com.slimenano.sdk.robot.exception.message.MessageTooLargeException;
import com.slimenano.sdk.robot.exception.permission.BotNoPermissionException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import com.slimenano.sdk.robot.messages.SNMessageChain;
import com.slimenano.sdk.robot.messages.content.SNAudio;
import com.slimenano.sdk.robot.messages.content.SNImage;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface SNContact {

    long getId();

    /**
     * 向目标发送消息
     *
     * @param robot 机器人实例
     * @param chain 消息链
     *
     * @return
     *
     * @throws UnsupportedRobotOperationException 机器人核心未实现该方法时抛出
     * @throws NoOperationPermissionException     插件没有操作权限时抛出
     * @throws MessageTooLargeException           当消息过长时抛出
     * @throws BotNoPermissionException           当机器人当前状态不能发送消息时抛出（被禁言）
     */
    SNMessageSource sendMessage(Robot robot, SNMessageChain chain) throws UnsupportedRobotOperationException, NoOperationPermissionException, MessageTooLargeException, BotNoPermissionException;

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
    SNImage uploadImg(Robot robot, File file)
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
    SNImage uploadImg(Robot robot, URL url, boolean forceUpdate)
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
    SNImage uploadImg(Robot robot, URL url)
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
    SNAudio uploadAudio(Robot robot, File file) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

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
    SNAudio uploadAudio(Robot robot, URL url) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

}
