package net.ipetty.android.boot;

import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

/**
 * 启动引导
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends BaseActivity {

	public final static String TAG = "SplashActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_splash);
		Log.i(TAG, "onCreate");
		init();

		new SplashTask(SplashActivity.this)
		// .setListener(new SplashTaskListener(SplashActivity.this))
				.execute();
	}

	private void init() {
		TextView version = (TextView) this.findViewById(R.id.version_info);
		String verStr = getResources().getString(R.string.app_version);
		String VersionName = String.format(verStr, AppUtils.getAppVersionName(this));
		version.setText(VersionName);
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
