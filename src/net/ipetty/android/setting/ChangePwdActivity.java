package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.R.layout;
import net.ipetty.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChangePwdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_pwd, menu);
		return true;
	}

}
