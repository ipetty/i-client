package net.ipetty.android.space;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.bonuspoint.BonusPointActivity;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.discover.DiscoverAdapter;
import net.ipetty.android.fans.FansActivity;
import net.ipetty.android.follow.FollowsActivity;
import net.ipetty.android.petty.PettyActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.GetUserStatisticsByUserId;
import net.ipetty.android.sdk.task.user.IsFollow;
import net.ipetty.android.user.UserActivity;
import net.ipetty.vo.UserStatisticsVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SpaceActivity extends Activity {

	public final static String TAG = SpaceActivity.class.getSimpleName();
	private Integer userId;
	private Integer currUserId;
	private Boolean isCurrentUser;
	private ViewFlipper viewFlipper;
	private View space_petty_view;
	private GridView space_photo_grid;
	private DiscoverAdapter space_photo_grid_adapter;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private TextView feed_num_text;
	private TextView follow_num_text;
	private TextView fan_num_text;
	private TextView action_bar_right_text;// 关注/修改个人信息
	private View follows;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space);

		currUserId = IpetApi.init(this).getCurrUserId();
		if (this.getIntent().getExtras() == null) {
			this.userId = currUserId;
		} else {
			this.userId = this.getIntent().getExtras().getInt(Constant.INTENT_USER_ID_KEY);
		}
		isCurrentUser = userId == currUserId;

		Log.d(TAG, "" + isCurrentUser);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		UserVO user = UserApiWithCache.getUserById4Synchronous(this, userId);

		String title = this.getResources().getString(R.string.title_activity_space);
		// 标题
		if (!isCurrentUser) {
			String title_space = getResources().getString(R.string.title_space);
			String username = user.getNickname();
			title = String.format(title_space, username);
		}
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(title);
		// 右侧操作按钮
		action_bar_right_text = (TextView) this.findViewById(R.id.action_bar_right_text);
		if (isCurrentUser) {
			action_bar_right_text.setText(R.string.user_edit);
			action_bar_right_text.setOnClickListener(userEditClick);
		} else {
			// 判断用户是否关注
			new IsFollow(this).setListener(new DefaultTaskListener<Boolean>(SpaceActivity.this) {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						action_bar_right_text.setText(R.string.follow_text);
					} else {
						action_bar_right_text.setText(R.string.unfollow_text);
					}
					action_bar_right_text.setOnClickListener(toggleFollowClick);
				}
			}).execute(this.userId);

		}

		// TODO:用户统计信息
		feed_num_text = (TextView) this.findViewById(R.id.feed_num_text);
		follow_num_text = (TextView) this.findViewById(R.id.follow_num_text);
		fan_num_text = (TextView) this.findViewById(R.id.fan_num_text);

		new GetUserStatisticsByUserId(this).setListener(new DefaultTaskListener<UserStatisticsVO>(SpaceActivity.this) {
			@Override
			public void onSuccess(UserStatisticsVO result) {
				feed_num_text.setText(String.valueOf(result.getFeedNum()));
				follow_num_text.setText(String.valueOf(result.getFollowerNum()));
				fan_num_text.setText(String.valueOf(result.getFollowerNum()));

			}
		}).execute(this.userId);

		// 头像
		ImageView avatar = (ImageView) this.findViewById(R.id.avatar);
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		}
		if (isCurrentUser) {
			avatar.setOnClickListener(userEditClick);
		}

		View fans = findViewById(R.id.fans_layout);
		fans.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpaceActivity.this, FansActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, SpaceActivity.this.userId);
				startActivity(intent);
			}
		});

		follows = findViewById(R.id.follows_layout);

		follows.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpaceActivity.this, FollowsActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, SpaceActivity.this.userId);
				startActivity(intent);
			}
		});

		View bonusPoint = findViewById(R.id.bonusPoint_layout);
		bonusPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SpaceActivity.this, BonusPointActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, SpaceActivity.this.userId);
				startActivity(intent);
			}
		});

		//
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		View space_petty_layout_btn = this.findViewById(R.id.space_petty_btn);
		View space_photo_layout_btn = this.findViewById(R.id.space_photo_btn);
		View space_feed_layout_btn = this.findViewById(R.id.space_feed_btn);

		space_petty_layout_btn.setOnClickListener(new TabClickListener(0));
		space_photo_layout_btn.setOnClickListener(new TabClickListener(1));
		space_feed_layout_btn.setOnClickListener(new TabClickListener(2));

		space_petty_view = this.findViewById(R.id.list_space_petty_item);
		View pet_edit_view = space_petty_view.findViewById(R.id.pet_edit_view);

		if (isCurrentUser) {
			pet_edit_view.setVisibility(View.VISIBLE);
			pet_edit_view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SpaceActivity.this, PettyActivity.class);
					// intent.putExtra("id", SpaceActivity.this.currUserId);
					startActivity(intent);
				}
			});
		}

		// 图形
		View space_photo_layout = this.findViewById(R.id.space_photo_layout);
		space_photo_grid = (GridView) space_photo_layout.findViewById(R.id.gridview);
		space_photo_grid_adapter = new DiscoverAdapter(this);
		space_photo_grid.setAdapter(space_photo_grid_adapter);
	}

	public class TabClickListener implements OnClickListener {

		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewFlipper.setDisplayedChild(index);
		}
	}

	private OnClickListener userEditClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SpaceActivity.this, UserActivity.class);
			// intent.putExtra("id", SpaceActivity.this.currUserId);
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
