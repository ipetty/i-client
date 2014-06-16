package net.ipetty.android.sdk.core;

/**
 * SDK层Exception.
 *
 */
public class APIException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 15055364153750405L;

    public APIException() {
        super();
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

}
