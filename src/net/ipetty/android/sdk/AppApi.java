package net.ipetty.android.sdk;

import net.ipetty.android.sdk.domain.IpetAppUpdate;

/**
 * 应用API
 * 
 * @author xiaojinghai
 */
public interface AppApi {

	/**
	 * 检查应用版本信息
	 */
	public IpetAppUpdate checkAppVersion(String appKey);

}
