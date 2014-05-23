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

public class SpaceActivity extends Activity {

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.space, menu);
		return true;
	}

}
