package net.ipetty.sharesdk;

import net.ipetty.android.login.Login3rdTaskListener;
import net.ipetty.android.sdk.task.user.Login3rd;
import net.ipetty.android.sdk.task.user.LoginOrRegister3rd;
import android.app.Activity;
import android.os.Message;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.utils.UIHandler;

/**
 * 第三方帐号登录抽象类
 * 
 * @author luocanfeng
 * @date 2014年7月24日
 */
public abstract class AbstractAuthorization {

	protected String TAG = getClass().getSimpleName();

	protected Activity activity;
	protected ShareSDKMessageCallback shareSDKMessageCallback;

	protected String userId;
	protected String userEmail;
	protected String userName;

	public AbstractAuthorization(Activity activity) {
		this.activity = activity;
		shareSDKMessageCallback = new ShareSDKMessageCallback(this.activity);
	}

	/**
	 * 使用第三方帐号登录
	 */
	public abstract void authorize(Platform platform);

	/**
	 * 使用已授权帐号登录我们的应用
	 */
	protected void login(Platform platform, String userId) {
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
	protected void registerOrLogin(Platform platform, String userId, String email, String userName) {
		Message msg = new Message();
		msg.what = ShareSDKConstant.MSG_LOGIN;
		msg.obj = platform;
		UIHandler.sendMessage(msg, shareSDKMessageCallback);
		new LoginOrRegister3rd(activity).setListener(new Login3rdTaskListener(activity, platform)).execute(
				platform.getName(), userId, email, userName);
	}

}
