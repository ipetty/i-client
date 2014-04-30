package net.ipetty.android.ui;

import net.ipetty.R;
import android.os.Bundle;
import android.view.Menu;

public class LoginHasAccountActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_has_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_has_account, menu);
		return true;
	}

}
