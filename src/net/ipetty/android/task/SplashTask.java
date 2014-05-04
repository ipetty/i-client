package net.ipetty.android.task;

import net.ipetty.android.ui.LoginHasAccountActivity;
import net.ipetty.android.ui.MainActivity;
import net.ipetty.android.ui.WelcomeRegisterOrLoginActivity;
import net.ipetty.android.utils.AnimUtils;
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
