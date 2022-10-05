package com.slimenano.framework.event.impl.bot;

import lombok.Getter;
import com.slimenano.framework.event.impl.ISysEvent;

public class LoginEvent extends ISysEvent<Object> {

    public static final int SUCCESS = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    @Getter
    private final String msg;

    @Getter
    private final int code;

    public LoginEvent(String msg, int code) {
        super(null);
        this.msg = msg;
        this.code = code;
    }

    public LoginEvent(String msg, int code, Throwable t) {
        super(t);
        this.msg = msg;
        this.code = code;
    }
}
