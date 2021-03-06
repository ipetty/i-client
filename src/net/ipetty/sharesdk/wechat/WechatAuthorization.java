package net.ipetty.sharesdk.wechat;

import java.util.HashMap;

import net.ipetty.sharesdk.AbstractAuthorization;
import net.ipetty.sharesdk.ShareSDKConstant;
import android.app.Activity;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;

/**
 * Share SDK 微信授权工具类
 * @author luocanfeng
 * @date 2015年1月16日
 */
public class WechatAuthorization extends AbstractAuthorization {

	public WechatAuthorization(Activity activity) {
		super(activity);
	}

	/**
	 * 使用第三方帐号登录
	 */
	public void authorize(Platform platform) {
		if (platform.isValid()) { // 指定平台已经完成授权
			userId = platform.getDb().getUserId();
			userName = platform.getDb().getUserName();
			String userGender = platform.getDb().getUserGender();
			String userIcon = platform.getDb().getUserIcon();
			String token = platform.getDb().getToken();
			String tokenSecret = platform.getDb().getTokenSecret();
			Log.v(TAG, "userId=" + userId + ",userName=" + userName + ",userGender=" + userGender + ",userIcon="
					+ userIcon + ",token=" + token + ",tokenSecret=" + tokenSecret);
			if (userId != null) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_USERID_FOUND, shareSDKMessageCallback);
				login(platform, userId);
				return;
			}
		} else {
			platform.setPlatformActionListener(new AuthorizeListener());
			// platform.SSOSetting(true); // true不使用SSO授权，false使用SSO授权
			platform.showUser(null);
		}
	}

	/**
	 * 帐号登录操作的Listener
	 */
	public class AuthorizeListener implements PlatformActionListener {

		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> result) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_AUTH_COMPLETE, shareSDKMessageCallback);
				Log.v(TAG, result.toString());
				userId = platform.getDb().getUserId();
				userName = platform.getDb().getUserName();
				String userGender = platform.getDb().getUserGender();
				String userIcon = platform.getDb().getUserIcon();
				String token = platform.getDb().getToken();
				String tokenSecret = platform.getDb().getTokenSecret();
				Log.v(TAG, "userId=" + userId + ",userName=" + userName + ",userGender=" + userGender + ",userIcon="
						+ userIcon + ",token=" + token + ",tokenSecret=" + tokenSecret);

				// 注册并登录
				registerOrLogin(platform, platform.getDb().getUserId(), null, userName);
			}
		}

		@Override
		public void onCancel(Platform platform, int action) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_AUTH_CANCEL, shareSDKMessageCallback);
			}
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_AUTH_ERROR, shareSDKMessageCallback);
			}
			Log.e(TAG, "授权操作出错", t);
			platform.removeAccount();
		}
	}

}
