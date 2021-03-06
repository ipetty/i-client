package net.ipetty.android.sdk.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.ResponseErrorHandler;

import android.util.Log;

/**
 * 异常处理
 * 
 * @author xiaojinghai
 */
public class ApiExceptionHandler implements ResponseErrorHandler {

	private String TAG = getClass().getSimpleName();

	private static final Charset charset = Charset.forName("utf-8");

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return hasError(response.getStatusCode());

	}

	protected boolean hasError(HttpStatus statusCode) {
		return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR || statusCode.series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		byte[] body = getResponseBody(response);
		HttpStatus statusCode = response.getStatusCode();
		Log.d(TAG, "statusCode:" + statusCode.series());
		String str = null;
		if (body != null) {
			str = new String(body, charset);
		}
		if (null == str || "".equals(str)) {
			str = "未知异常";
		}
		throw new APIException(str);
		// switch (statusCode.series()) {
		// case CLIENT_ERROR:
		// Log.d(TAG, "throw HttpClientErrorException:");
		// throw new HttpClientErrorException(statusCode,
		// response.getStatusText(), body,
		// charset);
		// case SERVER_ERROR:
		// Log.d(TAG, "throw HttpServerErrorException:");
		// throw new HttpServerErrorException(statusCode,
		// response.getStatusText(), body,
		// charset);
		// default:
		// Log.d(TAG, "throw RestClientException:");
		// throw new RestClientException("Unknown status code [" + statusCode +
		// "]");
		// }
	}

	private byte[] getResponseBody(ClientHttpResponse response) {
		try {
			InputStream responseBody = response.getBody();
			if (responseBody != null) {
				return FileCopyUtils.copyToByteArray(responseBody);
			}
		} catch (IOException ex) {
			// ignore
		}
		return new byte[0];
	}

}
