package top.bioelectronic.sdk.framework.ui;

public interface GUI_CONST {

    int OK = 1;
    int CANCEL = 2;
    int YES = 4;
    int NO = 8;
    int RETRY = 16;

    int OK_CANCEL = OK | CANCEL;
    int YES_NO = YES | NO;
    int OK_CANCEL_RETRY = OK_CANCEL | RETRY;
    int YES_NO_RETRY = YES_NO | RETRY;

    int INFO = 32;
    int WARN = 64;
    int ERROR = 128;


}
