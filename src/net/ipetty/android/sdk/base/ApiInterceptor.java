package net.ipetty.android.sdk.base;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Api请求拦截
 * 
 * @author xiaojinghai
 */
class ApiInterceptor implements ClientHttpRequestInterceptor {

	private final String appKey;

	private final String appSecret;

	private final Charset charset;

	public ApiInterceptor(String appKey, String appSecret) {
		this(appKey, appSecret, Charset.forName("UTF-8"));
	}

	public ApiInterceptor(String appKey, String appSecret, Charset charset) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.charset = charset;
	}

	/**
	 * 在每个情求中加入Basic验证头信息
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
//		request.getHeaders().set("Authorization",
//				"Basic " + new String(Base64.encode((appKey + ":" + appSecret).getBytes(charset)), charset));
		request.getHeaders().setAcceptEncoding(ContentCodingType.GZIP);
		return execution.execute(request, body);
	}

}
