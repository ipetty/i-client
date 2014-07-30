package net.ipetty.android.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;

public class LocationActivity extends BaseActivity {

	private String TAG = LocationActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

}
