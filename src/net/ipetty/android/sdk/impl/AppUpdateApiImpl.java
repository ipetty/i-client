package net.ipetty.android.sdk.impl;

import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.AppUpdateApi;
import net.ipetty.vo.AppUpdateVO;

import org.springframework.util.LinkedMultiValueMap;

import android.content.Context;

/**
 * AppUpdateApiImpl
 * 
 * @author luocanfeng
 * @date 2014年7月7日
 */
public class AppUpdateApiImpl extends ApiBase implements AppUpdateApi {

	public AppUpdateApiImpl(Context context) {
		super(context);
	}

	private static final String URI_GET_LATEST_VERSION = "/app/checkUpdate";

	/**
	 * 检查应用版本信息
	 */
	@Override
	public AppUpdateVO latestVersion(String appKey) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("appKey", appKey);
		return getRestTemplate().postForObject(buildUri(URI_GET_LATEST_VERSION), request, AppUpdateVO.class);
	}

}
