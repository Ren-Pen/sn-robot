package com.slimenano.sdk.robot.messages;

import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.exception.file.OverFileSizeMaxException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import com.slimenano.sdk.robot.messages.content.SNPoke;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface Plus {

    SNMessageChain plus(SNMessage message);

    SNMessageChain text(String text);

    SNMessageChain text(String text, Object... args);

    SNMessageChain atAll();

    SNMessageChain at(long target);

    SNMessageChain face(int id);

    SNMessageChain image(String id);

    SNMessageChain image(Robot robot, SNContact contact, File file) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    SNMessageChain image(Robot robot, SNContact contact, URL url) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    SNMessageChain flashImage(String id);

    SNMessageChain flashImage(Robot robot, SNContact contact, File file) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    SNMessageChain flashImage(Robot robot, SNContact contact, URL url) throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException;

    SNMessageChain poke(SNPoke poke);

    SNMessageChain ChuoYiChuo();

    SNMessageChain BiXin();

    SNMessageChain DianZan();

    SNMessageChain XinSui();

    SNMessageChain LiuLiuLiu();

    SNMessageChain FangDaZhao();

    SNMessageChain BaoBeiQiu();

    SNMessageChain Rose();

    SNMessageChain ZhaoHuanShu();

    SNMessageChain RangNiPi();

    SNMessageChain JieYin();

    SNMessageChain ShouLei();

    SNMessageChain GouYin();

    SNMessageChain Quote(SNMessageSource source);


}
