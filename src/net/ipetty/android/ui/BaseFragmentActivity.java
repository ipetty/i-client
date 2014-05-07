package net.ipetty.android.ui;

import net.ipetty.android.utils.ActivityUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
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
