package net.ipetty.android.boot;

import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.login.WelcomeRegisterOrLoginActivity;
import net.ipetty.android.main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SplashTask extends AsyncTask<Integer, Integer, Integer> {
	public final static String TAG = "SplashTask";
	private Activity activity;

	public SplashTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			//UserVO u = IpetApi.init(activity).getUserApi().login("luocanfeng@ipetty.net", "888888");
			//Log.i(TAG,"登录用户："+u.getEmail());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		this.goWelcomeLogin();
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
