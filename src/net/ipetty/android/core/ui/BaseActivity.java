package net.ipetty.android.core.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import net.ipetty.android.core.ActivityManager;

public class BaseActivity extends Activity {

	private String TAG = getClass().getSimpleName();

	private boolean isViewReady = false;

	private Bundle savedInstanceState;

	public void showMessageForShortTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showMessageForLongTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		ActivityManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		ActivityManager.getInstance().distoryActivity(this);
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			//只调用一次onViewReady
			if (!isViewReady) {
				onViewReady(this.savedInstanceState);
				onViewStart();
				onViewResume();
			}
			isViewReady = true;
		}
	}

	//界面初始化完毕，只触发一次
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
		if (isViewReady) {
			onViewStart();
		}
	}

	//ready情况下调用
	protected void onViewStart() {
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		if (isViewReady) {
			onViewResume();
		}
	}

	//ready情况下调用
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
		if (isViewReady) {
			onViewRestart();
		}
	}

	//ready情况下调用
	protected void onViewRestart() {
		Log.d(TAG, "onViewRestart");
	}

}
