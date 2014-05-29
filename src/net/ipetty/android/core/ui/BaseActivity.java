package net.ipetty.android.core.ui;

import net.ipetty.android.core.util.ActivityUtils;
import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtils.getInstance().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		ActivityUtils.getInstance().distoryActivity(this);
		super.onDestroy();
	}

}
