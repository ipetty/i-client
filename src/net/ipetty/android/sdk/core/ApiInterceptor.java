package net.ipetty.android.sdk.core;

import java.io.IOException;
import java.nio.charset.Charset;

import net.ipetty.android.core.util.DeviceUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

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

    private final String TAG = "ApiInterceptor";

    private final Charset charset = Charset.forName("UTF-8");

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

        //发送头
        String userToken = StateManager.getUserToken(context);
        String refreshToken = StateManager.getRefreshToken(context);
        String uuidB64 = StateManager.getDeviceUUID(context);
        if (StringUtils.isBlank(uuidB64)) {
            String uuid = DeviceUtils.getDeviceUUID(context).toString();
            uuidB64 = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
            StateManager.setDeviceUUID(context, uuidB64);
        }

        Log.i(TAG, "userToken：" + userToken);
        Log.i(TAG, "refreshToken：" + refreshToken);
        Log.i(TAG, "deviceUUID：" + uuidB64);

        HttpHeaders requestHeaders = request.getHeaders();
        requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);
        requestHeaders.set(HEADER_NAME_USER_TOKEN, userToken);
        requestHeaders.set(HEADER_NAME_REFRESH_TOKEN, refreshToken);
        requestHeaders.set(HEADER_NAME_DEVICE_UUID, uuidB64);

        String url = request.getURI().toString();
        Log.i(TAG, "-->：" + url);
        Log.i(TAG, "Etag头：" + request.getHeaders().getIfNoneMatch());

        ClientHttpResponse resp = execution.execute(request, body);

        //接收头
        HttpHeaders responseHeaders = resp.getHeaders();
        String rut = responseHeaders.getFirst(HEADER_NAME_USER_TOKEN);
        String rrt = responseHeaders.getFirst(HEADER_NAME_USER_TOKEN);
        Log.i(TAG, "接收到userToken:" + rut);
        Log.i(TAG, "接收到refreshToken:" + rrt);
        if (StringUtils.isNotBlank(rut) && !userToken.equals(rut)) {
            StateManager.setUserToken(context, rut);
        }
        if (StringUtils.isNotBlank(rrt) && !refreshToken.equals(rrt)) {
            StateManager.setRefreshToken(context, rrt);
        }

        Log.i(TAG, "<--:" + request.getURI().toString());
        Log.i(TAG, "Etag头：" + resp.getHeaders().getETag());
        Log.i(TAG, "状态：" + resp.getRawStatusCode());

        //Log.i(TAG, "-->" + request.getURI() + ":" + resp.getRawStatusCode());
        return resp;
    }

}
