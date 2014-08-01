package net.ipetty.android.sdk.impl;

import net.ipetty.android.core.Constant;
import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.NotificationApi;
import net.ipetty.vo.NotificationVO;
import android.content.Context;

/**
 * NotificationApiImpl
 * 
 * @author luocanfeng
 * @date 2014年8月1日
 */
public class NotificationApiImpl extends ApiBase implements NotificationApi {

	public NotificationApiImpl(Context context) {
		super(context);
	}

	private static final String URI_GET_MY_NOTIFICATION = "/notification";

	/**
	 * 获取我的通知
	 */
	public NotificationVO getMyNotification() {
		super.requireAuthorization();

		return getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_GET_MY_NOTIFICATION, NotificationVO.class);
	}

}
