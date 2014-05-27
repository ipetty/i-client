package net.ipetty.android.ui;

import net.ipetty.R;
import net.ipetty.android.ui.event.BackClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class SpaceActivity extends Activity {
	public final static String TAG = "SpaceActivity";
	private ViewFlipper viewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		//
		String title = this.getResources().getString(R.string.title_activity_space);
		text.setText(title);
		btnBack.setOnClickListener(new BackClickListener(this));

		View fans = findViewById(R.id.fans_layout);
		fans.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpaceActivity.this, FansActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		View follows = findViewById(R.id.follows_layout);
		follows.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpaceActivity.this, FollowsActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		View bonusPoint = findViewById(R.id.bonusPoint_layout);
		bonusPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpaceActivity.this, BonusPointActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		//
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		View space_petty_layout = this.findViewById(R.id.space_petty_btn);
		View space_photo_layout = this.findViewById(R.id.space_photo_btn);
		View space_feed_layout = this.findViewById(R.id.space_feed_btn);
		space_petty_layout.setOnClickListener(new TabClickListener(0));
		space_photo_layout.setOnClickListener(new TabClickListener(1));
		space_feed_layout.setOnClickListener(new TabClickListener(2));
	}

	public class TabClickListener implements OnClickListener {
		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewFlipper.setDisplayedChild(index);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.space, menu);
		return true;
	}

}
