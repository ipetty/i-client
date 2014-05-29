package net.ipetty.android.boot;

import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
/**
 * 启动引导
 * @author Administrator
 *
 */
public class SplashActivity extends BaseActivity {
	public final static String TAG = "SplashActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Log.i(TAG, "onCreate");
		init();
		

		
		new SplashTask(SplashActivity.this).execute();
	}

	private void init() {
		TextView version = (TextView) this.findViewById(R.id.version_info);
		String verStr = getResources().getString(R.string.app_version);
		String VersionName = String.format(verStr, AppUtils.getAppVersionName(this));
		version.setText(VersionName);
	}
	
	// 转向主界面
	public void goMain() {
		Log.i(TAG, "to MainActivity");
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		AnimUtils.fadeInToOut(this);
		this.finish();
	}

	// 转向登陆界面
	public void goWelcomeLogin() {
		Log.i(TAG, "to WelcomeRegisterOrLoginActivity");
		Intent intent = new Intent(this, WelcomeRegisterOrLoginActivity.class);
		this.startActivity(intent);
		AnimUtils.fadeInToOut(this);
		this.finish();
	}

	// 转向已有账号登陆
	public void goHasAccountLogin() {
		Log.i(TAG, "to LoginHasAccountActivity");
		Intent intent = new Intent(this, LoginHasAccountActivity.class);
		this.startActivity(intent);
		AnimUtils.fadeInToOut(this);
		this.finish();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

}
