package net.ipetty.android.petty;

import net.ipetty.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PettyActivity extends Activity {
	public final static String TAG = "PettyActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petty);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.petty, menu);
		return true;
	}

}
