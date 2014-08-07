package net.ipetty.android.sdk.impl;

import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.CrashLogApi;
import net.ipetty.vo.CrashLogVO;
import android.content.Context;

/**
 * CrashLogApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月4日
 */
public class CrashLogApiImpl extends ApiBase implements CrashLogApi {

	public CrashLogApiImpl(Context context) {
		super(context);
	}

	private static final String URI_SAVE_CRASH_LOG = "/crash";

	/**
	 * 保存崩溃日志
	 */
	public boolean save(CrashLogVO crashLog) {
		return getRestTemplate().postForObject(buildUri(URI_SAVE_CRASH_LOG), crashLog, Boolean.class);
	}

}
