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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_splash);
		Log.d(TAG, "onCreate");
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
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

}
