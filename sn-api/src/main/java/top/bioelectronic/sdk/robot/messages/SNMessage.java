package top.bioelectronic.sdk.robot.messages;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.messages.content.*;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;
import top.bioelectronic.sdk.robot.messages.meta.SNQuoteReply;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static top.bioelectronic.sdk.robot.messages.content.SNPoke.*;

public abstract class SNMessage implements Plus {

    @Override
    public SNMessageChain plus(SNMessage msg) {
        return this.toChain().plus(msg);
    }

    @Override
    public SNMessageChain text(String text) {
        return this.toChain().text(text);
    }

    @Override
    public SNMessageChain text(String text, Object... args) {
        return this.toChain().text(text, args);
    }

    public SNMessageChain toChain() {
        return new SNMessageChain().plus(this);
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
    public SNMessageChain image(Robot robot, File file) throws IOException {
        return plus(robot.uploadImg(file));
    }

    @Override
    public SNMessageChain image(Robot robot, URL url) throws IOException {
        return plus(robot.uploadImg(url));
    }


    @Override
    public SNMessageChain flashImage(String id) {
        return plus(new SNFlashImage(new SNImage(id)));
    }

    @Override
    public SNMessageChain flashImage(Robot robot, File file) throws IOException {
        return plus(new SNFlashImage(robot.uploadImg(file)));
    }

    @Override
    public SNMessageChain flashImage(Robot robot, URL url) throws IOException {
        return plus(new SNFlashImage(robot.uploadImg(url)));
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

}
