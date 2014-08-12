package net.ipetty.android.feed;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationActivity extends BaseActivity {
	private View layout0;
	private View layout1;
	private View layout2;
	private View layout3;
	private View layout4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_location));
		btnBack.setOnClickListener(new BackClickListener(this));

		layout0 = this.findViewById(R.id.layout0);
		layout1 = this.findViewById(R.id.layout1);
		layout2 = this.findViewById(R.id.layout2);
		layout3 = this.findViewById(R.id.layout3);
		layout4 = this.findViewById(R.id.layout4);

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		layout0.setVisibility(View.VISIBLE);
		TextView item0 = (TextView) layout0.findViewById(R.id.item);
		// item0.setText(text)

	}

}
