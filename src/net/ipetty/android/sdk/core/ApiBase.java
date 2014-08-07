package net.ipetty.android.sdk.core;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.URIBuilder;
import net.ipetty.android.sdk.cache.RestTemplate4Cache;
import net.ipetty.vo.UserVO;
import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * API基类, 提供统一RestTemplate对象和一些常用方法
 *
 * @author xiaojinghai
 */
public class ApiBase {

	private String TAG = getClass().getSimpleName();

	private final RestTemplate restTemplate;

	protected final Context context;

	private static final LinkedMultiValueMap<String, String> EMPTY_PARAMETERS = new LinkedMultiValueMap<String, String>();

	public ApiBase(Context ctx) {
		this.context = ctx;
		// restTemplate = new RestTemplateWrap(ctx);
		Charset charset = Charset.forName("UTF-8");

		restTemplate = new RestTemplate4Cache(context, 50, 5000);
		// 关于HTTP组件的选择：http://www.07net01.com/program/653485.html
		/**
		 * SimpleClientHttpRequestFactory HttpURLConnection --推荐
		 *
		 * CommonsClientHttpRequestFactory CommonsClientHttpRequest --不推荐
		 *
		 * HttpComponentsClientHttpRequestFactory HttpUriRequest --?
		 */

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		// HttpComponentsClientHttpRequestFactory factory = new
		// HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(60 * 1000);
		factory.setReadTimeout(60 * 1000);

		// 避免HttpURLConnection的http.keepAlive Bug
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}

		restTemplate.setRequestFactory(factory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter(charset));
		// messageConverters.add(new MappingJackson2HttpMessageConverter());

		MappingJacksonHttpMessageConverter mjm = new MappingJacksonHttpMessageConverter();
		mjm.getObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mjm.getObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		messageConverters.add(mjm);

		restTemplate.setMessageConverters(messageConverters);

		restTemplate.setErrorHandler(new ApiExceptionHandler());
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new ApiInterceptor(context));
		restTemplate.setInterceptors(interceptors);
	}

	protected URI buildUri(String path) {
		return buildUri(path, EMPTY_PARAMETERS);
	}

	protected URI buildUri(String path, String parameterName, String parameterValue) {
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.set(parameterName, parameterValue);
		return buildUri(path, parameters);
	}

	protected URI buildUri(String path, MultiValueMap<String, String> parameters) {
		return URIBuilder.fromUri(Constant.API_SERVER_BASE + path).queryParams(parameters).build();
	}

	protected void requireAuthorization() {
		if (!getIsAuthorized()) {
			throw new APIException("用户未登录");
		}
	}

	public boolean getIsAuthorized() {
		return SDKStateManager.getAuthorized(context);
	}

	protected void setIsAuthorized(boolean bl, String platformName) {
		SDKStateManager.setAuthorized(context, bl);
		// 设置退出时清空UserToken、RefreshToken、PlatformName
		if (!bl) {
			SDKStateManager.setUserToken(context, "");
			SDKStateManager.setRefreshToken(context, "");
			SDKStateManager.setPlatformName(context, "");
		} else {
			SDKStateManager.setPlatformName(context, platformName);
		}
	}

	public UserVO getCurrUserInfo() {
		return SDKStateManager.getCurrentUserInfo(context);
	}

	public void setCurrUserInfo(UserVO user) {
		SDKStateManager.setCurrentUserInfo(context, user);
	}

	public int getCurrUserId() {
		return SDKStateManager.getUid(context);
	}

	public void setCurrUserId(Integer uid) {
		SDKStateManager.setUid(context, uid);
	}

	private Boolean result = false;

	/**
	 * 检测服务器服务是否可用
	 */
	public synchronized Boolean checkServiceAvaliable() {
		final CountDownLatch latch = new CountDownLatch(1);
		result = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(Constant.API_HEALTH_URL);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setUseCaches(false);
					conn.setConnectTimeout(60 * 1000);
					conn.setReadTimeout(60 * 1000);
					// conn.setRequestProperty("Connection", "close");
					conn.connect();
					// 获取内容长度
					int length = conn.getContentLength();
					Log.d(TAG, "checkServiceAvaliable:" + length);
					if (length > 0) {
						result = true;
					}
					latch.countDown();
				} catch (MalformedURLException ex) {
					latch.countDown();
					result = false;
					Log.e(TAG, "", ex);
				} catch (IOException ex) {
					latch.countDown();
					result = false;
					Log.e(TAG, "", ex);
				} catch (Exception ex) {
					latch.countDown();
					result = false;
					Log.e(TAG, "", ex);
				}

			}
		}).start();
		try {
			latch.await();
			return result;
		} catch (InterruptedException ex) {
			throw new APIException("服务检测异常", ex);
		}

	}

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

}
