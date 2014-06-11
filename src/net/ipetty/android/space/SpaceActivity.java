package net.ipetty.android.space;

import net.ipetty.R;
import net.ipetty.android.bonuspoint.BonusPointActivity;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.fans.FansActivity;
import net.ipetty.android.follow.FollowsActivity;
import net.ipetty.android.user.UserActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
		btnBack.setOnClickListener(new BackClickListener(this));

		String title = this.getResources().getString(R.string.title_activity_space);
		// 标题
		if (!isCurrentUser()) {
			String title_space = getResources().getString(R.string.title_space);
			String username = "张三";
			title = String.format(title_space, username);
		}
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(title);
		// 右侧操作按钮
		TextView action_bar_right_text = (TextView) this.findViewById(R.id.action_bar_right_text);
		if (isCurrentUser()) {
			action_bar_right_text.setText(R.string.user_edit);
			action_bar_right_text.setOnClickListener(userEditClick);
		} else {
			// TODO:判断用户是否关注
			if (true) {
				action_bar_right_text.setText(R.string.follow_text);
			} else {
				action_bar_right_text.setText(R.string.unfollow_text);
			}
			action_bar_right_text.setOnClickListener(toggleFollowClick);
		}

		// 头像
		ImageView avatar = (ImageView) this.findViewById(R.id.avatar);
		if (isCurrentUser()) {
			avatar.setOnClickListener(userEditClick);
		}

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

	private boolean isCurrentUser() {
		// TODO 判断是否当前用户
		return true;
	}

	private OnClickListener userEditClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SpaceActivity.this, UserActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener toggleFollowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO 关注与反关注的操作
			TextView view = (TextView) v;
			Toast.makeText(SpaceActivity.this, "暂无", Toast.LENGTH_SHORT).show();
		}
	};

}
