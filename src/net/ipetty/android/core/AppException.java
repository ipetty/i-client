package net.ipetty.android.core;

/**
 * UI层异常
 *
 *
 */
public class AppException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 15055364153750405L;

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

}
