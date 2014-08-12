package net.ipetty.android.sdk.cache;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.URI;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.sdk.core.APIException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import static org.springframework.http.HttpMethod.GET;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 *
 * @author xiaojinghai
 */
public class RestTemplate4Cache extends RestTemplate {

	private String TAG = getClass().getSimpleName();

	private static final String ETAG_HEADER = "ETag";

	// private final MemoryLRUCache cache = new MemoryLRUCache();
	private final Cache4L2 cache;

	private final Context context;

	public RestTemplate4Cache(Context ctx, int maxNumL1, int maxNumL2) {
		super();
		context = ctx;
		cache = new Cache4L2(ctx, maxNumL1, maxNumL2);
	}

	@Override
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		// 如果网络不可用，直接从缓存获取数据
		if (!NetWorkUtils.isNetworkConnected(context)) {

			if (!isCacheableRequest(method)) {
				Log.d(TAG, "离线状态，只能浏览:" + url);
				throw new APIException("您当前处于离线状态！");
			}

			CacheEntry e = getCache().get(url.toString());

			if (null != e) {
				Log.d(TAG, "离线，找到离线缓存:" + url + ":" + e.getValue());
				T t = JSONUtils.fromJSON(e.getValue(), e.getClassType());
				return t;
			} else {
				Log.d(TAG, "离线，没找到离线缓存:" + url);
				throw new APIException("没找到离线缓存");
			}

		}
		// 非get请求，则透传给父类正常请求
		if (!isCacheableRequest(method)) {
			Log.d(TAG, "在线，非Get方法:" + method + ":" + url);
			return super.doExecute(url, method, requestCallback, responseExtractor);
		}

		CacheEntry e = getCache().get(url.toString());
		// 找到没有过期的缓存，则直接返回结果，不产生请求
		if (null != e && System.currentTimeMillis() < e.getExpireOn()) {
			Log.d(TAG, "在线，未过期,直接从缓存取值:" + url);
			T t = JSONUtils.fromJSON(e.getValue(), e.getClassType());
			return t;
		}

		// 如果是get请求，则使用自定义的代理对像，以处理缓存
		Log.d(TAG, "在线，Get方法,使用Etag请求:" + url);
		return super.doExecute(url, method, new DelegatingRequestCallback(url, requestCallback),
				new DelegatingResponseExtractor<T>(url, method, responseExtractor));
	}

	private boolean isCacheableRequest(HttpMethod method) {

		return GET.equals(method);
	}

	/**
	 * @return the cache
	 */
	public Cache4L2 getCache() {
		return cache;
	}

	/**
	 * 支持缓存的回调 请求前对Request对象进行处理
	 */
	private class DelegatingRequestCallback implements RequestCallback {

		private final URI uri;
		private final RequestCallback callback;

		public DelegatingRequestCallback(URI uri, RequestCallback callback) {

			Assert.notNull(uri);

			this.uri = uri;
			this.callback = callback;
		}

		public void doWithRequest(ClientHttpRequest request) throws IOException {
			// 如果之前有缓存则增加IF_NONE_MATCH_HEADER头
			CacheEntry e = getCache().get(uri.toString());
			if (null != e) {
				Log.d(TAG, "doWithRequest-->增加Etag头:" + e.getEtag());
				request.getHeaders().setIfNoneMatch(e.getEtag());
			}

			if (null != callback) {
				callback.doWithRequest(request);
			}
		}
	}

	private class DelegatingResponseExtractor<T> implements ResponseExtractor<T> {

		private final URI uri;
		private final HttpMethod method;
		private final ResponseExtractor<T> extractor;

		public DelegatingResponseExtractor(URI uri, HttpMethod method, ResponseExtractor<T> extractor) {

			Assert.notNull(uri);

			this.uri = uri;
			this.method = method;
			this.extractor = extractor;
		}

		public T extractData(ClientHttpResponse response) throws IOException {

			HttpHeaders headers = response.getHeaders();

			boolean isNotModified = NOT_MODIFIED.equals(response.getStatusCode());
			// 如果返回304状态，则直接从缓存取值
			if (isNotModified) {
				CacheEntry e = getCache().get(uri.toString());
				Log.d(TAG, "extractData-->304,从缓存取:" + e.getValue());
				T t = JSONUtils.fromJSON(e.getValue(), e.getClassType());
				return t;
			}

			T result = extractor.extractData(response);

			// 如果返回200状态并且带有etag头
			if (isCacheableRequest(method) && headers.containsKey(ETAG_HEADER)
					&& HttpStatus.OK.equals(response.getStatusCode())) {
				// 处理Etag
				String eTag = response.getHeaders().getETag();
				eTag = eTag == null ? "-1" : eTag;

				// 处理CacheControl
				String cacheControl = response.getHeaders().getCacheControl();
				Long expireOn = System.currentTimeMillis();
				if (null != cacheControl && !"".equals(cacheControl)) {
					String[] t = cacheControl.split("=");
					String s = null == t[1] ? "0" : t[1];
					expireOn = System.currentTimeMillis() + (Long.valueOf(s) * 1000);
				}
				// 临时处理，只为避免客户端1秒内发起重复请求，不利用这个特性做较长时间的缓存
				expireOn = System.currentTimeMillis() + (1000);

				String str = JSONUtils.toJson(result);
				String classType = result.getClass().getName();
				Log.d(TAG, "extractData-->200,放入缓存:" + str);
				getCache().put(new CacheEntry(uri.toString(), str, eTag, expireOn, classType));

			}

			return result;
		}
	}

}
