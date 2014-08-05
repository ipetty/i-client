package net.ipetty.android.core.ui;

import net.ipetty.android.core.ActivityManager;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.DelayTask;
import net.ipetty.android.core.ErrorHandler;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity {

	protected String TAG = getClass().getSimpleName();

	private boolean isViewReady = false;

	private Bundle savedInstanceState;

	private ErrorHandler errorHandler;

	private final int delayTime = 500;

	public void showMessageForShortTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showMessageForLongTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		errorHandler = new ErrorHandler(this);
		isViewReady = false;
		ActivityManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		ActivityManager.getInstance().distoryActivity(this);
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			new DelayTask(this).setListener(new DefaultTaskListener<Void>(this) {
				@Override
				public void onSuccess(Void result) {
					// 只调用一次onViewReady
					if (!isViewReady) {
						onViewReady(savedInstanceState);
						onViewStart();
						onViewResume();
					}
					isViewReady = true;
				}
			}).execute(delayTime);

		}
	}

	// 界面初始化完毕，只触发一次
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
		if (isViewReady) {
			try {
				onViewStart();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	// ready情况下调用
	protected void onViewStart() {
		Log.d(TAG, "onViewStart");
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		if (isViewReady) {
			try {
				onViewResume();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	// ready情况下调用
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart");
		super.onRestart();
		if (isViewReady) {
			try {
				onViewRestart();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	// ready情况下调用
	protected void onViewRestart() {
		Log.d(TAG, "onViewRestart");
	}
}
