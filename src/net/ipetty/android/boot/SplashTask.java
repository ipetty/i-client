package net.ipetty.android.boot;

import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.register.Register3rdActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.util.Log;

public class SplashTask extends Task<Void, Void> {

	public SplashTask(BaseActivity activity) {
		super(activity);
	}

	// 重写onPreExecute防止出现loading...
	@Override
	protected void onPreExecute() {

	}

	@Override
	protected Void myDoInBackground(Void... args) {
		IpetApi api = IpetApi.init(activity);
		// 是否就已认证状态
		if (api.getIsAuthorized()) {
			Integer currUserId = api.getCurrUserId();
			UserVO currentUser = UserApiWithCache.getUserById4Synchronous(activity, currUserId);
			if (currentUser != null && StringUtils.isNotEmpty(currentUser.getEmail())) { // 跳转到首页
				goMain();
			} else { // 跳转到完善资料页面
				AppUtils.goTo(activity, Register3rdActivity.class);
				activity.finish();
			}
		} else {
			// 以前没有登录过
			if (api.getCurrUserId() == Constant.EMPTY_USER_ID) {
				// 欢迎界面
				goWelcomeLogin();
			} else {
				// 有过登录帐号
				goHasAccountLogin();
			}
		}
		// try {
		// //启动防止白屏,给时间显示出启动画面
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// throw new AppException("启动错误", e);
		// }

		return null;
	}

	// 转向主界面
	public void goMain() {
		Log.d(TAG, "to MainActivity");
		AppUtils.goTo(activity, MainActivity.class);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

	// 转向登陆界面
	public void goWelcomeLogin() {
		Log.d(TAG, "to WelcomeRegisterOrLoginActivity");
		AppUtils.goTo(activity, WelcomeRegisterOrLoginActivity.class);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

	// 转向已有账号登陆
	public void goHasAccountLogin() {
		Log.d(TAG, "to LoginHasAccountActivity");
		AppUtils.goTo(activity, LoginHasAccountActivity.class);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

}
