package net.ipetty.android.space;

import java.text.SimpleDateFormat;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.bonuspoint.BonusPointActivity;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.discover.DiscoverAdapter;
import net.ipetty.android.discover.DiscoverRender;
import net.ipetty.android.fans.FansActivity;
import net.ipetty.android.follow.FollowsActivity;
import net.ipetty.android.home.FeedAdapter;
import net.ipetty.android.home.LargerImageActivity;
import net.ipetty.android.petty.PettyActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.ListByTimelineForSpace;
import net.ipetty.android.sdk.task.foundation.GetOptionValueLabelMap;
import net.ipetty.android.sdk.task.foundation.SetOptionLabelTaskListener;
import net.ipetty.android.sdk.task.pet.ListPetsByUserId;
import net.ipetty.android.sdk.task.user.Follow;
import net.ipetty.android.sdk.task.user.GetUserStatisticsByUserId;
import net.ipetty.android.sdk.task.user.IsFollow;
import net.ipetty.android.sdk.task.user.Unfollow;
import net.ipetty.android.user.UserActivity;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.PetVO;
import net.ipetty.vo.UserStatisticsVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SpaceActivity extends BaseActivity {

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Integer userId;
	private Integer currUserId;
	private Boolean isCurrentUser;

	private Integer petId;

	private ViewFlipper viewFlipper;
	private View space_petty_view;
	private GridView space_photo_grid;
	private DiscoverRender space_photo_render;
	private DiscoverAdapter space_photo_grid_adapter;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private TextView feed_num_text;
	private TextView follow_num_text;
	private TextView fan_num_text;
	private TextView action_bar_right_text;// 关注/修改个人信息
	private View follows;
	private ImageView avatar;// 头像
	private Boolean isFollow;// 记录当前用户是否已关注被查看用户
	private Integer pageNumber = 0;// 我的feed
	private final Integer pageSize = 30;// 我的feed
	private View space_feed_layout; // feed列表布局
	private ListView space_feed_list;// feed列表View
	private FeedAdapter feedListAdapter;// feed列表适配
	private Long lastTimeMillis;
	private ImageView header_bg;

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private void getRefreshTime() {
		Log.d(TAG, "getRefreshTime");
		if (NetWorkUtils.isNetworkConnected(this)) {
			lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Space(this, lastTimeMillis);
			return;
		}

		lastTimeMillis = MyAppStateManager.getLastRefrsh4Space(this);
		if (lastTimeMillis == -1l) {
			lastTimeMillis = System.currentTimeMillis();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space);

		currUserId = IpetApi.init(this).getCurrUserId();
		if (this.getIntent().getExtras() == null) {
			this.userId = currUserId;
		} else {
			this.userId = this.getIntent().getExtras().getInt(Constant.INTENT_USER_ID_KEY);
		}
		isCurrentUser = userId == currUserId;
		Log.d(TAG, "userId：" + userId);
		Log.d(TAG, "currUserId：" + currUserId);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		btnBack.setOnClickListener(new BackClickListener(this));

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROADCAST_INTENT_FEED_COMMENT);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_FAVORED);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_PUBLISH);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_DELETE);
		filter.addAction(Constant.BROADCAST_INTENT_CCOMMENT_DELETE);
		this.registerReceiver(broadcastreciver, filter);

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		getRefreshTime();
		final UserVO user = UserApiWithCache.getUserById4Synchronous(this, userId);
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
		View action_bar_right_divider = this.findViewById(R.id.action_bar_right_divider);
		action_bar_right_text = (TextView) this.findViewById(R.id.action_bar_right_text);
		if (isCurrentUser) {
			action_bar_right_text.setText(R.string.user_edit);
			action_bar_right_divider.setVisibility(View.VISIBLE);
			action_bar_right_text.setOnClickListener(userEditClick);
		} else {
			// 判断用户是否关注
			new IsFollow(this).setListener(new DefaultTaskListener<Boolean>(SpaceActivity.this) {
				@Override
				public void onSuccess(Boolean result) {
					SpaceActivity.this.isFollow = result;
					if (SpaceActivity.this.isFollow) {
						action_bar_right_text.setText(R.string.unfollow_text);
					} else {
						action_bar_right_text.setText(R.string.follow_text);
					}
					action_bar_right_text.setOnClickListener(toggleFollowClick);
				}
			}).execute(this.userId);

		}

		header_bg = (ImageView) this.findViewById(R.id.header_bg);

		// 用户统计信息
		feed_num_text = (TextView) this.findViewById(R.id.feed_num_text);
		follow_num_text = (TextView) this.findViewById(R.id.follow_num_text);
		fan_num_text = (TextView) this.findViewById(R.id.fan_num_text);

		new GetUserStatisticsByUserId(this).setListener(new DefaultTaskListener<UserStatisticsVO>(SpaceActivity.this) {
			@Override
			public void onSuccess(UserStatisticsVO userStatistics) {
				feed_num_text.setText(String.valueOf(userStatistics.getFeedNum()));
				follow_num_text.setText(String.valueOf(userStatistics.getFriendsNum()));
				fan_num_text.setText(String.valueOf(userStatistics.getFollowerNum()));
			}
		}).execute(this.userId);

		// 头像
		avatar = (ImageView) this.findViewById(R.id.avatar);
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}

		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isNotBlank(user.getAvatar())) {
					Intent intent = new Intent(SpaceActivity.this, LargerImageActivity.class);
					intent.putExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY, Constant.FILE_SERVER_BASE + user.getAvatar());
					intent.putExtra(Constant.INTENT_IMAGE_SAMILL_KEY, Constant.FILE_SERVER_BASE + user.getAvatar());
					SpaceActivity.this.startActivity(intent);
				}
			}
		});

		View feeds = findViewById(R.id.feeds_layout);
		feeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewFlipper.setDisplayedChild(2);
			}
		});

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

		// Tab索引
		viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		View space_petty_layout_btn = this.findViewById(R.id.space_petty_btn);
		View space_photo_layout_btn = this.findViewById(R.id.space_photo_btn);
		View space_feed_layout_btn = this.findViewById(R.id.space_feed_btn);

		space_petty_layout_btn.setOnClickListener(new TabClickListener(0));
		space_photo_layout_btn.setOnClickListener(new TabClickListener(1));
		space_feed_layout_btn.setOnClickListener(new TabClickListener(2));

		/* ==== init space_petty_view start ==== */
		space_petty_view = this.findViewById(R.id.list_space_petty_item);
		loadPetsData();
		/* ==== init space_petty_view end ==== */

		// 图形
		View space_photo_layout = this.findViewById(R.id.space_photo_layout);
		space_photo_render = new DiscoverRender(this);

		// 列表
		space_feed_layout = this.findViewById(R.id.space_feed_layout);
		space_feed_list = (ListView) space_feed_layout.findViewById(R.id.space_feed_list);
		feedListAdapter = new FeedAdapter(this);
		space_feed_list.setAdapter(feedListAdapter);
		// refreshData();

	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshData();
		}
	};

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

	// 关注与反关注动作
	private OnClickListener toggleFollowClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (SpaceActivity.this.isFollow) {
				new Unfollow(SpaceActivity.this).setListener(
						new DefaultTaskListener<Boolean>(SpaceActivity.this, "正在反关注...") {
							@Override
							public void onSuccess(Boolean result) {
								SpaceActivity.this.isFollow = false;
								refreshData();
							}
						}).execute(SpaceActivity.this.userId);
			} else {
				new Follow(SpaceActivity.this).setListener(
						new DefaultTaskListener<Boolean>(SpaceActivity.this, "正在关注...") {
							@Override
							public void onSuccess(Boolean result) {
								SpaceActivity.this.isFollow = true;
								refreshData();
							}
						}).execute(SpaceActivity.this.userId);
			}
		}
	};

	// 加载宠物信息
	private void loadPetsData() {
		Log.d(TAG, "loadPetsData");
		new ListPetsByUserId(SpaceActivity.this).setListener(new DefaultTaskListener<List<PetVO>>(SpaceActivity.this) {
			@Override
			public void onSuccess(List<PetVO> pets) {
				if (CollectionUtils.isEmpty(pets)) {
					// TODO 无宠物情况下的展现
					return;
				}

				// TODO 多个宠物的展现
				// 只展现一个宠物
				final PetVO pet = pets.get(0);

				// FIXME 多个宠物时，目前这种赋值方法明显是错误的
				SpaceActivity.this.petId = pet.getId();

				ImageView petAvatar = (ImageView) space_petty_view.findViewById(R.id.pet_avatar);
				if (StringUtils.isNotBlank(pet.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + pet.getAvatar(), petAvatar,
							options);
				} else {
					petAvatar.setImageResource(R.drawable.avatar);
				}

				petAvatar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (StringUtils.isNotBlank(pet.getAvatar())) {
							Intent intent = new Intent(SpaceActivity.this, LargerImageActivity.class);
							intent.putExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY,
									Constant.FILE_SERVER_BASE + pet.getAvatar());
							intent.putExtra(Constant.INTENT_IMAGE_SAMILL_KEY,
									Constant.FILE_SERVER_BASE + pet.getAvatar());
							SpaceActivity.this.startActivity(intent);
						}
					}
				});

				TextView petName = (TextView) space_petty_view.findViewById(R.id.pet_name);
				petName.setText(pet.getNickname() == null ? "" : pet.getNickname());

				TextView petGender = (TextView) space_petty_view.findViewById(R.id.pet_sex);
				// TODO 缓存OptionValueLabelMap
				if (StringUtils.isNotBlank(pet.getGender())) {
					new GetOptionValueLabelMap(SpaceActivity.this).setListener(
							new SetOptionLabelTaskListener(SpaceActivity.this, petGender, pet.getGender())).execute(
							OptionGroup.PET_GENDER);
				}

				TextView petBirthday = (TextView) space_petty_view.findViewById(R.id.pet_birthday);
				if (pet.getBirthday() != null) {
					petBirthday.setText(dateFormat.format(pet.getBirthday()));
				}

				TextView petFamily = (TextView) space_petty_view.findViewById(R.id.pet_family);
				// TODO 缓存OptionValueLabelMap
				if (StringUtils.isNotBlank(pet.getFamily())) {
					header_bg.setImageResource(Constant.PET_FAMILY_RES_MAP.get(pet.getFamily()));

					new GetOptionValueLabelMap(SpaceActivity.this).setListener(
							new SetOptionLabelTaskListener(SpaceActivity.this, petFamily, pet.getFamily())).execute(
							OptionGroup.PET_FAMILY);
				}

				if (isCurrentUser) {
					View pet_edit_view = space_petty_view.findViewById(R.id.pet_edit_view);
					pet_edit_view.setVisibility(View.VISIBLE);
					pet_edit_view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(SpaceActivity.this, PettyActivity.class);
							intent.putExtra(Constant.INTENT_PET_ID_KEY, SpaceActivity.this.petId);
							startActivity(intent);
						}
					});
				}
			}
		}).execute(SpaceActivity.this.userId);
	}

	@Override
	public void onViewResume() {
		Log.d(TAG, "onViewResume");
		refreshData();
	}

	private void refreshData() {
		Log.d(TAG, "refreshData");
		Log.d(TAG, "userId:" + userId);
		UserVO user = UserApiWithCache.getUserById4Synchronous(this, userId);
		// 头像
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}
		// 统计数据
		new GetUserStatisticsByUserId(this).setListener(new DefaultTaskListener<UserStatisticsVO>(SpaceActivity.this) {
			@Override
			public void onSuccess(UserStatisticsVO userStatistics) {
				feed_num_text.setText(String.valueOf(userStatistics.getFeedNum()));
				follow_num_text.setText(String.valueOf(userStatistics.getFriendsNum()));
				fan_num_text.setText(String.valueOf(userStatistics.getFollowerNum()));
			}
		}).execute(this.userId);

		// 宠物信息
		loadPetsData();

		// 关注/反关注图标
		if (!isCurrentUser) {
			// 判断用户是否关注
			new IsFollow(this).setListener(new DefaultTaskListener<Boolean>(SpaceActivity.this) {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						action_bar_right_text.setText(R.string.unfollow_text);
					} else {
						action_bar_right_text.setText(R.string.follow_text);
					}
					action_bar_right_text.setOnClickListener(toggleFollowClick);
				}
			}).execute(this.userId);
		}

		new ListByTimelineForSpace(this).setListener(new DefaultTaskListener<List<FeedVO>>(SpaceActivity.this) {
			@Override
			public void onSuccess(List<FeedVO> result) {
				// space_photo_grid_adapter.loadDate(result);
				space_photo_render.loadData(result);
				feedListAdapter.setList(result);
				feedListAdapter.notifyDataSetChanged();
			}
		}).execute(userId.toString(), lastTimeMillis.toString(), "0", pageSize.toString());

		// 9宫格
		// space_photo_grid_adapter.notifyDataSetChanged();
		// feed列表
		// feedListAdapter.notifyDataSetChanged();

		View space_content = this.findViewById(R.id.space_content);
		space_content.setVisibility(View.VISIBLE);
	}
}
