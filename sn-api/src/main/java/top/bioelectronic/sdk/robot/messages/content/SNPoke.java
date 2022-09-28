package top.bioelectronic.sdk.robot.messages.content;

import lombok.Getter;
import top.bioelectronic.sdk.robot.messages.SNContentMessage;

@Getter
public class SNPoke extends SNContentMessage {

    public static final SNPoke ChuoYiChuo = new SNPoke("戳一戳", 1, -1);
    public static final SNPoke BiXin = new SNPoke("比心", 2, -1);
    public static final SNPoke DianZan = new SNPoke("点赞", 3, -1);
    public static final SNPoke XinSui = new SNPoke("心碎", 4, -1);
    public static final SNPoke LiuLiuLiu = new SNPoke("666", 5, -1);
    public static final SNPoke FangDaZhao = new SNPoke("放大招", 6, -1);
    public static final SNPoke BaoBeiQiu = new SNPoke("宝贝球", 126, 2011);
    public static final SNPoke Rose = new SNPoke("玫瑰花", 126, 2007);
    public static final SNPoke ZhaoHuanShu = new SNPoke("召唤术", 126, 2006);
    public static final SNPoke RangNiPi = new SNPoke("让你皮", 126, 2009);
    public static final SNPoke JieYin = new SNPoke("结印", 126, 2005);
    public static final SNPoke ShouLei = new SNPoke("手雷", 126, 2004);
    public static final SNPoke GouYin = new SNPoke("勾引", 126, 2003);
    public static final SNPoke ZhuaYiXia = new SNPoke("抓一下", 126, 2001);
    public static final SNPoke SuiPing = new SNPoke("碎屏", 126, 2002);
    public static final SNPoke QiaoMen = new SNPoke("敲门", 126, 2002);


    private final int id;
    private final int pokeType;
    private final String name;

    public SNPoke(int id, int pokeType, String name) {
        this.id = id;
        this.pokeType = pokeType;
        this.name = name;
    }

    public SNPoke(int id, int pokeType) {
        this.id = id;
        this.pokeType = pokeType;
        this.name = null;
    }


    private SNPoke(String name, int pokeType, int id) {
        this.name = name;
        this.pokeType = pokeType;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SNPoke miraiPoke = (SNPoke) o;

        if (id != miraiPoke.id) return false;
        return pokeType == miraiPoke.pokeType;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + pokeType;
        return result;
    }
}
