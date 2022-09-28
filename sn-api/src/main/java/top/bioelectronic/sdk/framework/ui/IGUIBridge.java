package top.bioelectronic.sdk.framework.ui;

/**
 * 界面桥
 */
public interface IGUIBridge extends GUI_CONST {

    boolean confirm(String title, String content, int type);

    void alert(String title, String content, int type);

    String prompt(String title, String content, String defaultValue);

    String getVersion();

    String getName();

    void main(String[] args);

}
