package net.ipetty.android.sdk.core;

/**
 * Service层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class APIException extends RuntimeException {

	/** serialVersionUID */
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
