package top.bioelectronic.sdk.access;

import lombok.Getter;

public enum Dangerous {

    // 临时代表仅在调试时可以调用的权限，正式发布版本将移除这些调用
    TEMP("临时", 3),
    NORMAL("正常", 2),
    PRIVACY("隐私", 1),
    DANGEROUS("高危", 0);
    @Getter
    private final String title;

    @Getter
    private final int val;


    Dangerous(String title, int val) {
        this.title = title;
        this.val = val;
    }
}
