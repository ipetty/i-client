package net.ipetty.android.sdk.core;

import java.io.IOException;
import java.nio.charset.Charset;

import net.ipetty.android.utils.DeviceUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import android.content.Context;
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
	
    private final String TAG = "ApiInterceptor";

    private final Charset charset = Charset.forName("UTF-8");
    
    private final Context context;
    
    public ApiInterceptor(Context context){
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
    	HttpHeaders requestHeaders = request.getHeaders();
    	requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);
    	requestHeaders.set(HEADER_NAME_USER_TOKEN, userToken);
    	requestHeaders.set(HEADER_NAME_REFRESH_TOKEN, refreshToken);
    	requestHeaders.set(HEADER_NAME_DEVICE_UUID, DeviceUtils.getDeviceUUID(context).toString());
    	
//        request.getHeaders().set("Authorization",
//                "Basic " + new String(new Base64().encode((Constant.APP_KEY + ":" + Constant.APP_SECRET).getBytes(charset)), charset));

        if (request.getMethod().equals(HttpMethod.GET)) {
            String url = request.getURI().toString();
            Log.i(TAG, "-->："+url);
            Log.i(TAG, "Etag头：" + request.getHeaders().getIfNoneMatch());
        }

        ClientHttpResponse resp = execution.execute(request, body);

        //接收头
        HttpHeaders responseHeaders = resp.getHeaders();
        String rut = responseHeaders.getFirst(HEADER_NAME_USER_TOKEN);
        String rrt = responseHeaders.getFirst(HEADER_NAME_USER_TOKEN);
        if (StringUtils.isNotBlank(rut)&&!userToken.equals(rut)) {
        	StateManager.setUserToken(context, rut);
		}
        if (StringUtils.isNotBlank(rrt)&&!refreshToken.equals(rrt)) {
        	StateManager.setRefreshToken(context, rrt);
		}
        
        if (request.getMethod().equals(HttpMethod.GET)) {
            Log.i(TAG, "<--:"+request.getURI().toString());
            Log.i(TAG, "Etag头：" + resp.getHeaders().getETag());
            Log.i(TAG, "状态：" + resp.getRawStatusCode());
        }

        //Log.i(TAG, "-->" + request.getURI() + ":" + resp.getRawStatusCode());
        return resp;
    }

}
