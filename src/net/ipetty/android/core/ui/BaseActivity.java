package net.ipetty.android.core.ui;

import net.ipetty.android.core.MyAppCrashHandler;
import net.ipetty.android.core.util.ActivityUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	private ProgressDialog progressDialog;

	private boolean destroyed = false;

	//显示异步加载提示对话框
	public void showProgressDialog(CharSequence message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setIndeterminate(true);
		}

		this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}

	//销毁异步加载提示对话框
	public void dismissProgressDialog() {
		if (this.progressDialog != null && !this.destroyed) {
			this.progressDialog.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtils.getInstance().addActivity(this);
		Thread.setDefaultUncaughtExceptionHandler(new MyAppCrashHandler(this));
	}

	@Override
	protected void onDestroy() {
		ActivityUtils.getInstance().distoryActivity(this);
		super.onDestroy();
	}

}
