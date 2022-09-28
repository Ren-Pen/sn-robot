package top.bioelectronic.sdk.robot.exceptions;

public class NoImplementException extends MiraiException{
    public NoImplementException() {
        super();
    }

    public NoImplementException(String message) {
        super(message);
    }

    public NoImplementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoImplementException(Throwable cause) {
        super(cause);
    }

    protected NoImplementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
