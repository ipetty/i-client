package net.ipetty.android.core.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class BackClickListener implements OnClickListener {
	private Activity activity;

	public BackClickListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		activity.finish();
	}

}
