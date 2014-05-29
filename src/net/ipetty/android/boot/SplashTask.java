package net.ipetty.android.boot;

import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class SplashTask extends AsyncTask<Integer, Integer, Integer> {
	public final static String TAG = "GetUserByIdTask";
	private Activity activity;

	public SplashTask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		try {
			IpetApi api= IpetApi.init(activity);
			
			if(api.getIsAuthorized()){
				//首页
				goMain();
			}else{
				//以前没有登录过
				if(api.getCurrUserId()==-1){
					//欢迎界面
					goWelcomeLogin();
				}else{
					//有过登录帐号
					goHasAccountLogin();
				}
			}
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		
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
