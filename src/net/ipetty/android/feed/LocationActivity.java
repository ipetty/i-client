package net.ipetty.android.feed;

import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import android.os.Bundle;
import android.view.Menu;

public class LocationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

}
