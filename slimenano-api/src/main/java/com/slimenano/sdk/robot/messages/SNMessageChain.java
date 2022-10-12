package com.slimenano.sdk.robot.messages;

import com.slimenano.sdk.robot.contact.SNContact;
import com.slimenano.sdk.robot.exception.file.OverFileSizeMaxException;
import com.slimenano.sdk.robot.exception.permission.NoOperationPermissionException;
import com.slimenano.sdk.robot.exception.unsupported.UnsupportedRobotOperationException;
import lombok.NoArgsConstructor;
import com.slimenano.sdk.core.Robot;
import com.slimenano.sdk.robot.messages.content.*;
import com.slimenano.sdk.robot.messages.meta.SNMessageSource;
import com.slimenano.sdk.robot.messages.meta.SNQuoteReply;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.slimenano.sdk.robot.messages.content.SNPoke.*;

@NoArgsConstructor
public class SNMessageChain extends ArrayList<SNMessage> implements Plus {


    public SNMessageChain(SNMessage... message) {
        addAll(Arrays.asList(message));
    }


    @Override
    public boolean add(SNMessage SNMessage) {
        if (SNMessage == null) return false;
        return super.add(SNMessage);
    }

    @Override
    public void add(int index, SNMessage element) {
        if (element == null) return;
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends SNMessage> c) {
        return super.addAll(c.stream().filter((Predicate<SNMessage>) Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public boolean addAll(int index, Collection<? extends SNMessage> c) {
        return super.addAll(index, c.stream().filter((Predicate<SNMessage>) Objects::nonNull).collect(Collectors.toList()));
    }

    public SNMessage[] get(Class<? extends SNMessage> msgClass) {
        return this.stream().filter(miraiMessage -> msgClass.isAssignableFrom(miraiMessage.getClass())).toArray(SNMessage[]::new);
    }

    public SNMessageChain plus(SNMessage SNMessage) {
        add(SNMessage);
        return this;
    }

    @Override
    public SNMessageChain text(String text) {
        return plus(new SNText(text));
    }

    @Override
    public SNMessageChain text(String text, Object... args) {
        return plus(new SNText(text, args));
    }

    @Override
    public SNMessageChain atAll() {
        return plus(new SNAtAll());
    }

    @Override
    public SNMessageChain at(long target) {
        return plus(new SNAt(target));
    }

    @Override
    public SNMessageChain face(int id) {
        return plus(new SNFace(id));
    }

    @Override
    public SNMessageChain image(String id) {
        return plus(new SNImage(id));
    }

    @Override
    public SNMessageChain image(Robot robot,SNContact contact, File file)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException{
        return plus(robot.uploadImg(contact, file));
    }

    @Override
    public SNMessageChain image(Robot robot, SNContact contact, URL url)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException {
        return plus(robot.uploadImg(contact, url));
    }

    @Override
    public SNMessageChain flashImage(String id) {
        return plus(new SNFlashImage(new SNImage(id)));
    }

    @Override
    public SNMessageChain flashImage(Robot robot,SNContact contact, File file)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException{
        return plus(new SNFlashImage(robot.uploadImg(contact, file)));
    }

    @Override
    public SNMessageChain flashImage(Robot robot,SNContact contact, URL url)
            throws IOException, UnsupportedRobotOperationException, NoOperationPermissionException, OverFileSizeMaxException{
        return plus(new SNFlashImage(robot.uploadImg(contact, url)));
    }

    @Override
    public SNMessageChain poke(SNPoke poke) {
        return plus(poke);
    }

    @Override
    public SNMessageChain ChuoYiChuo() {
        return plus(ChuoYiChuo);
    }

    @Override
    public SNMessageChain BiXin() {
        return plus(BiXin);
    }

    @Override
    public SNMessageChain DianZan() {
        return plus(DianZan);
    }

    @Override
    public SNMessageChain XinSui() {
        return plus(XinSui);
    }

    @Override
    public SNMessageChain LiuLiuLiu() {
        return plus(LiuLiuLiu);
    }

    @Override
    public SNMessageChain FangDaZhao() {
        return plus(FangDaZhao);
    }

    @Override
    public SNMessageChain BaoBeiQiu() {
        return plus(BaoBeiQiu);
    }

    @Override
    public SNMessageChain Rose() {
        return plus(Rose);
    }

    @Override
    public SNMessageChain ZhaoHuanShu() {
        return plus(ZhaoHuanShu);
    }

    @Override
    public SNMessageChain RangNiPi() {
        return plus(RangNiPi);
    }

    @Override
    public SNMessageChain JieYin() {
        return plus(JieYin);
    }

    @Override
    public SNMessageChain ShouLei() {
        return plus(ShouLei);
    }

    @Override
    public SNMessageChain GouYin() {
        return plus(GouYin);
    }

    @Override
    public SNMessageChain Quote(SNMessageSource source) {
        return plus(new SNQuoteReply(source));
    }

    public boolean contains(Class<? extends SNMessage> clazz) {
        return this.stream().anyMatch(miraiMessage -> miraiMessage.getClass() == clazz);
    }

    public boolean contains(SNMessage message) {
        return this.stream().anyMatch(miraiMessage -> miraiMessage.equals(message));
    }

}
