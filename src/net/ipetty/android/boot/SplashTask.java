package net.ipetty.android.boot;

import net.ipetty.android.core.MyAsyncTask;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import android.content.Intent;
import android.util.Log;

public class SplashTask extends MyAsyncTask<Void,Void>{
	public final static String TAG = "SplashTask";

	public SplashTask(BaseActivity activity) {
		super(activity);
	}
	
	//重写onPreExecute防止出现loading...
	@Override
	protected void onPreExecute() {
		
	}

	@Override
	protected Void doInBackground(Void... args) {
			try {
				//延时让用户看到启动画面
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			
			IpetApi api = IpetApi.init(activity);
			//是否就已认证状态
			if (api.getIsAuthorized()) {
				// 首页
				goMain();
			} else {
				// 以前没有登录过
				if (api.getCurrUserId() == -1) {
					// 欢迎界面
					goWelcomeLogin();
				} else {
					// 有过登录帐号
					goHasAccountLogin();
				}
			}

		
		return null;
	}


	// 转向主界面
	public void goMain() {
		Log.i(TAG, "to MainActivity");
		Intent intent = new Intent(this.activity, MainActivity.class);
		this.activity.startActivity(intent);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

	// 转向登陆界面
	public void goWelcomeLogin() {
		Log.i(TAG, "to WelcomeRegisterOrLoginActivity");
		Intent intent = new Intent(this.activity, WelcomeRegisterOrLoginActivity.class);
		this.activity.startActivity(intent);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

	// 转向已有账号登陆
	public void goHasAccountLogin() {
		Log.i(TAG, "to LoginHasAccountActivity");
		Intent intent = new Intent(this.activity, LoginHasAccountActivity.class);
		this.activity.startActivity(intent);
		AnimUtils.fadeInToOut(this.activity);
		this.activity.finish();
	}

}
