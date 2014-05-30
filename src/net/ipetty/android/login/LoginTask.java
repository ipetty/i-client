package net.ipetty.android.login;

import net.ipetty.android.boot.WelcomeRegisterOrLoginActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.main.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<Void, Void, Void> {
	public final static String TAG = "GetUserByIdTask";
	private Activity activity;
	private String loginName;
	private String password;

	public LoginTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected Void doInBackground(Void...voids ) {
		// TODO Auto-generated method stub
		try {
			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
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
