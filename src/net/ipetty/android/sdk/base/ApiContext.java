package net.ipetty.android.sdk.base;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;

/**
 * 应用容器，单例，线程安全，存放API范围内公共变量
 *
 * @author xiaojinghai
 */
public class ApiContext {
    
    private final RestTemplate restTemplate;
    
    private Boolean isAuthorized;
    
    private String currUserId;

    // 文件服务器地址
    public static final String FILE_SERVER_BASE = "http://api.ipetty.net/";
	// public static final String FILE_SERVER_BASE =
    // "http://192.168.253.1:8080/";

    // API服务器地址
    public static final String API_SERVER_BASE = "http://api.ipetty.net/api/v1/";
    // public static final String API_SERVER_BASE =
    // "http://192.168.253.1:8080/api/v1/";

    private static ApiContext instance;
    
    private final Context androidContext;
    
    private ApiContext(String appKey, String appSecret, Context androidContext) {
        this.androidContext = androidContext;
        //isAuthorized = LoginManager.isLogin(androidContext);
        Charset charset = Charset.forName("UTF-8");
        
        restTemplate = new RestTemplate();
        // 避免HttpURLConnection的http.keepAlive Bug
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(10 * 1000);
        factory.setReadTimeout(30 * 1000);
        restTemplate.setRequestFactory(factory);
        
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(charset));
        // messageConverters.add(new MappingJackson2HttpMessageConverter());

        MappingJacksonHttpMessageConverter mjm = new MappingJacksonHttpMessageConverter();
        mjm.getObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverters.add(mjm);
        
        restTemplate.setMessageConverters(messageConverters);
        
        restTemplate.setErrorHandler(new ApiExceptionHandler());
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new ApiInterceptor(appKey, appSecret));
        restTemplate.setInterceptors(interceptors);
        /*
         * if (restTemplate.getRequestFactory() instanceof
         * SimpleClientHttpRequestFactory) { ((SimpleClientHttpRequestFactory)
         * restTemplate .getRequestFactory()).setConnectTimeout(10 * 1000);
         * ((SimpleClientHttpRequestFactory) restTemplate
         * .getRequestFactory()).setReadTimeout(10 * 1000); } else if
         * (restTemplate.getRequestFactory() instanceof
         * HttpComponentsClientHttpRequestFactory) {
         * 
         * ((HttpComponentsClientHttpRequestFactory) restTemplate
         * .getRequestFactory()).setReadTimeout(10 * 1000);
         * ((HttpComponentsClientHttpRequestFactory) restTemplate
         * .getRequestFactory()).setConnectTimeout(10 * 1000); }
         */
    }
    
    public static synchronized ApiContext getInstace(String appKey, String appSecret, Context androidContext) {
        if (instance == null) {
            instance = new ApiContext(appKey, appSecret, androidContext);
        }
        return instance;
    }
    
    public synchronized RestTemplate getRestTemplate() {
        return restTemplate;
    }
    
    public synchronized Boolean getIsAuthorized() {
        //return LoginManager.isLogin(androidContext);
    	//TODO:
    	throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }
    
    public synchronized void setIsAuthorized(Boolean isAuthorized) {
        //LoginManager.setLogin(androidContext, isAuthorized);
    	//TODO:
    	throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }
    
    public synchronized String getCurrUserId() {
        //return LoginManager.getUid(androidContext);
    	//TODO:
    	throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }
    
    public synchronized void setCurrUserId(String currUser) {
        //LoginManager.setUid(androidContext, currUser);
    	//TODO:
    	throw new UnsupportedOperationException("Invalid operation for sorted list.");
    }
    
}
