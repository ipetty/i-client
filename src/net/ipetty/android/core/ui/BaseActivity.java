package net.ipetty.android.core.ui;

import net.ipetty.android.core.ActivityManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

public class BaseActivity extends Activity {

	public void showMessageForShortTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	public void showMessageForLongTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		ActivityManager.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		ActivityManager.getInstance().distoryActivity(this);
		super.onDestroy();
	}

}
