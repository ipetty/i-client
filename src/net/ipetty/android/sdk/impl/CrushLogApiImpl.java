package net.ipetty.android.sdk.impl;

import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.CrushLogApi;
import android.content.Context;

/**
 * CrushLogApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrushLogApiImpl extends ApiBase implements CrushLogApi {

	public CrushLogApiImpl(Context context) {
		super(context);
	}

	private static final String URI_SAVE_CRUSH_LOG = "/crush";

	/**
	 * 保存崩溃日志
	 */
	public boolean save(String log) {
		return getRestTemplate().postForObject(buildUri(URI_SAVE_CRUSH_LOG, "log", log), null, Boolean.class);
	}

}
