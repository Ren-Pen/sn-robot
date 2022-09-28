package top.bioelectronic.sdk.robot.exceptions;

public class MiraiException extends RuntimeException{
    public MiraiException() {
        super();
    }

    public MiraiException(String message) {
        super(message);
    }

    public MiraiException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiraiException(Throwable cause) {
        super(cause);
    }

    protected MiraiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
