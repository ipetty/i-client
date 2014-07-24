package net.ipetty.sharesdk.sinaweibo;

import java.util.HashMap;

import net.ipetty.android.login.Login3rdTaskListener;
import net.ipetty.android.sdk.task.user.Login3rd;
import net.ipetty.android.sdk.task.user.LoginOrRegister3rd;
import net.ipetty.sharesdk.ShareSDKConstant;
import net.ipetty.sharesdk.ShareSDKMessageCallback;
import android.app.Activity;
import android.os.Message;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Share SDK 新浪微博授权工具类
 * 
 * @author luocanfeng
 * @date 2014年7月21日
 */
public class SinaWeiboAuthorization {

	private final static String TAG = SinaWeiboAuthorization.class.getSimpleName();

	private Activity activity;
	private SinaWeiboAuthorizeListener authorizeListener;
	private GetSinaWeiboEmailListener getSinaWeiboEmailListener;
	private ShareSDKMessageCallback shareSDKMessageCallback;

	private String userId;
	private String email;
	private String userName;

	public SinaWeiboAuthorization(Activity activity) {
		super();
		this.activity = activity;
		authorizeListener = new SinaWeiboAuthorizeListener();
		getSinaWeiboEmailListener = new GetSinaWeiboEmailListener();
		shareSDKMessageCallback = new ShareSDKMessageCallback(this.activity);
	}

	/**
	 * 使用第三方帐号登录
	 */
	public void authorize(Platform platform) {
		if (platform.isValid()) { // 指定平台已经完成授权
			userId = platform.getDb().getUserId();
			userName = platform.getDb().getUserName();
			// String userGender = platform.getDb().getUserGender();
			// String userIcon = platform.getDb().getUserIcon();
			// String token = platform.getDb().getToken();
			// String tokenSecret = platform.getDb().getTokenSecret();
			// Log.v(TAG, "userId=" + userId + ",userName=" + userName +
			// ",userGender=" + userGender + ",userIcon="
			// + userIcon + ",token=" + token + ",tokenSecret=" + tokenSecret);
			if (userId != null) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_USERID_FOUND, shareSDKMessageCallback);
				login(platform, userId);
				return;
			}
		} else {
			// platform.removeAccount();
			platform.setPlatformActionListener(authorizeListener);
			// platform.SSOSetting(true); // true不使用SSO授权，false使用SSO授权
			platform.showUser(null);
		}
	}

	/**
	 * 使用已授权帐号登录我们的应用
	 */
	private void login(Platform platform, String userId) {
		Message msg = new Message();
		msg.what = ShareSDKConstant.MSG_LOGIN;
		msg.obj = platform;
		UIHandler.sendMessage(msg, shareSDKMessageCallback);
		new Login3rd(activity).setListener(new Login3rdTaskListener(activity, platform)).execute(platform.getName(),
				userId);
	}

	/**
	 * 使用已授权帐号登录我们的应用（需要在之前授权完成后将用户信息写入我们的帐号数据库中）
	 */
	private void login(Platform platform, String userId, String email, String userName) {
		Message msg = new Message();
		msg.what = ShareSDKConstant.MSG_LOGIN;
		msg.obj = platform;
		UIHandler.sendMessage(msg, shareSDKMessageCallback);
		new LoginOrRegister3rd(activity).setListener(new Login3rdTaskListener(activity, platform)).execute(
				platform.getName(), userId, email, userName);
	}

	/**
	 * 新浪微博帐号登录操作的Listener
	 */
	public class SinaWeiboAuthorizeListener implements PlatformActionListener {
		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> result) {
			if (action == Platform.ACTION_USER_INFOR) {
				UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_AUTH_COMPLETE, shareSDKMessageCallback);
				Log.v(TAG, result.toString());
				userName = (String) result.get("name");

				Platform sinaWeibo = ShareSDK.getPlatform(activity, SinaWeibo.NAME);
				HashMap<String, Object> params = new HashMap<String, Object>();
				sinaWeibo.setPlatformActionListener(getSinaWeiboEmailListener);
				sinaWeibo.customerProtocol("https://api.weibo.com/2/account/profile/email.json", "GET", (short) 1,
						params, null);
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

	/**
	 * 获取新浪微博邮箱帐号Listener
	 */
	public class GetSinaWeiboEmailListener implements PlatformActionListener {
		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> result) {
			Log.v(TAG, result.toString());
			email = "test1231987412974172@gmail.com";
			// email = (String) result.get("email");

			login(platform, platform.getDb().getUserId(), email, userName);
		}

		@Override
		public void onCancel(Platform platform, int action) {
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			Log.e(TAG, "获取新浪微博邮箱帐号出错", t);
			platform.removeAccount();
		}
	}

}
