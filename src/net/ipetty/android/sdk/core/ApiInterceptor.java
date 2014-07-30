package net.ipetty.android.sdk.core;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import java.io.IOException;
import net.ipetty.android.core.util.DeviceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
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

	public static final String HEADER_NAME_USER_TOKEN = "user_token";

	public static final String HEADER_NAME_REFRESH_TOKEN = "refresh_token";

	public static final String HEADER_NAME_DEVICE_UUID = "device_uuid";

	public static final String HEADER_NAME_DEVICE_ID = "device_id";

	public static final String HEADER_NAME_DEVICE_MAC = "device_mac";

	private final String TAG = ApiInterceptor.class.getSimpleName();

	// private final Charset charset = Charset.forName("UTF-8");
	private final Context context;

	public ApiInterceptor(Context context) {
		this.context = context;
	}

	/**
	 * 在每个情求中加入Basic验证头信息
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		String userToken = SDKStateManager.getUserToken(context);
		String refreshToken = SDKStateManager.getRefreshToken(context);
		String uuidB64 = SDKStateManager.getDeviceUUID(context);
		if (StringUtils.isBlank(uuidB64)) {
			String uuid = DeviceUtils.getDeviceUUID(context).toString();
			uuidB64 = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
			SDKStateManager.setDeviceUUID(context, uuidB64);
		}
		String url = request.getURI().toString();
		Log.d(TAG, "-->：" + url);
		Log.d(TAG, "userToken：" + userToken);
		Log.d(TAG, "refreshToken：" + refreshToken);
		Log.d(TAG, "deviceUUID：" + uuidB64);
		// 发送头
		HttpHeaders requestHeaders = request.getHeaders();
		requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);
		requestHeaders.set(HEADER_NAME_USER_TOKEN, userToken);
		requestHeaders.set(HEADER_NAME_REFRESH_TOKEN, refreshToken);
		requestHeaders.set(HEADER_NAME_DEVICE_UUID, uuidB64);
		requestHeaders.set("Connection", "close");

//		Boolean isServiceAvaliable = IpetApi.init(context).checkServiceAvaliable();
//		if (!isServiceAvaliable) {
//			Log.d(TAG, "throw ServiceUnavailableException");
//			throw new ServiceUnavailableException();
//		}
		//执行请求
		ClientHttpResponse resp = execution.execute(request, body);

		Log.d(TAG, "<--:" + request.getURI().toString());
		// 接收头
		HttpHeaders responseHeaders = resp.getHeaders();
		String rut = responseHeaders.getFirst(HEADER_NAME_USER_TOKEN);
		String rrt = responseHeaders.getFirst(HEADER_NAME_REFRESH_TOKEN);
		Log.d(TAG, "接收到userToken:" + rut);
		Log.d(TAG, "接收到refreshToken:" + rrt);
		if (StringUtils.isNotBlank(rut) && !userToken.equals(rut)) {
			Log.d(TAG, "设置userToken:" + rut);
			SDKStateManager.setUserToken(context, rut);
		}
		if (StringUtils.isNotBlank(rrt) && !refreshToken.equals(rrt)) {
			Log.d(TAG, "设置refreshToken:" + rrt);
			SDKStateManager.setRefreshToken(context, rrt);
		}

//		if (StringUtils.isBlank(resp.getHeaders().getCacheControl())) {
//			resp.getHeaders().add("Cache-Control", "max-age=15");
//		}
		Log.d(TAG, "Etag头：" + resp.getHeaders().getETag());
		Log.d(TAG, "过期头：" + resp.getHeaders().getCacheControl());
		Log.d(TAG, "状态：" + resp.getRawStatusCode());

		// Log.d(TAG, "-->" + request.getURI() + ":" + resp.getRawStatusCode());
		return resp;
	}

}
