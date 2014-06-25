package net.ipetty.android.feed;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleFeedActivity extends Activity {
	public final static String TAG = SimpleFeedActivity.class.getSimpleName();
	private Long feedId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_feed);

		Log.i(TAG, "onCreate");
		feedId = this.getIntent().getExtras().getLong(Constant.INTENT_FEED_ID_KEY);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_simple_feed));
		btnBack.setOnClickListener(new BackClickListener(this));

	}

}
