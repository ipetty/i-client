package net.ipetty.android.sdk.base;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpMethod;
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
		request.getHeaders().set("Authorization",
				"Basic " + new String( new Base64().encode((appKey + ":" + appSecret).getBytes(charset)), charset));
		request.getHeaders().setAcceptEncoding(ContentCodingType.GZIP);
		/*
		if (request.getMethod().equals(HttpMethod.GET)) {
            request.getHeaders().setIfNoneMatch("\"0e5a13c33f077c25d9b259992332b90cf\"");
            //request.getHeaders().setETag("0e5a13c33f077c25d9b259992332b90cf");
            String rheads = request.getHeaders().toSingleValueMap().toString();
            String url = request.getURI().toString();
            String param = request.getURI().getQuery();
            String content = new String(body, charset);
            System.out.println("--->发送：");
            System.out.println("头：" + rheads);
            System.out.println("地址：" + url);
            System.out.println("参数：" + param);
            System.out.println("内容：" + content);
        }*/
		
		 ClientHttpResponse response = execution.execute(request, body);
		/*
		if (request.getMethod().equals(HttpMethod.GET)) {
            String rheads = response.getHeaders().toSingleValueMap().toString();
            int status = response.getRawStatusCode();
            String rbody = IOUtils.toString(response.getBody(), charset.toString());
            System.out.println("接受<---");
            System.out.println("头：" + rheads);
            System.out.println("状态：" + status);
            System.out.println("内容：" + rbody);
        }*/
		
		return response;
	}

}
