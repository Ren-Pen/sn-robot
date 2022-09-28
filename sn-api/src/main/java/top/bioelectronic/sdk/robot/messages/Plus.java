package top.bioelectronic.sdk.robot.messages;

import top.bioelectronic.sdk.core.Robot;
import top.bioelectronic.sdk.robot.messages.content.SNPoke;
import top.bioelectronic.sdk.robot.messages.meta.SNMessageSource;

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

    SNMessageChain image(Robot robot, File file) throws IOException;

    SNMessageChain image(Robot robot, URL url) throws IOException;

    SNMessageChain flashImage(String id);

    SNMessageChain flashImage(Robot robot, File file) throws IOException;

    SNMessageChain flashImage(Robot robot, URL url) throws IOException;

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
