package net.ipetty.android.sdk.core;

/**
 * 服务不可用
 *
 */
public class ServiceUnavailableException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 15055364153750405L;

	public ServiceUnavailableException() {
		super();
	}

	public ServiceUnavailableException(String message) {
		super(message);
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceUnavailableException(Throwable cause) {
		super(cause);
	}

}
