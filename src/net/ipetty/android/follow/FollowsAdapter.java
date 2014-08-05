package net.ipetty.android.follow;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.sdk.task.user.Follow;
import net.ipetty.android.sdk.task.user.IsFollow;
import net.ipetty.android.sdk.task.user.Unfollow;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FollowsAdapter extends BaseAdapter implements OnScrollListener {

	private String TAG = getClass().getSimpleName();

	private LayoutInflater inflater;
	private List<UserVO> list = new ArrayList<UserVO>(); // 这个就本地dataStore
	private int userId;
	private int currUserId;
	private Boolean isCurrentUser;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private Context context;

	public FollowsAdapter(Context context, int userId, int currUserId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.userId = userId;
		this.currUserId = currUserId;
		isCurrentUser = userId == currUserId;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return list.get(position).getId();
	}

	// 构建一个布局缓存的结构体 与VO对应
	// 构建一个布局缓存的结构体 与VO对应
	public class ViewHolder {

		public ImageView avatar;
		public TextView name;
		public ImageView follow;
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "list position-->" + position);
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			Log.d(TAG, "init items View");
			view = inflater.inflate(R.layout.list_follows_item, null);
			holder = new ViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.follow = (ImageView) view.findViewById(R.id.follow);

			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 数据与界面绑定
		final UserVO user = list.get(position);
		// 昵称
		holder.name.setText(user.getNickname());
		// 昵称事件
		holder.name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				context.startActivity(intent);
			}
		});
		// 头像
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance()
					.displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), holder.avatar, options);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}
		// 头像事件
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				context.startActivity(intent);
			}
		});
		// 关注按钮
		final ImageView followImageView = holder.follow;
		new IsFollow((Activity) this.context).setListener(new DefaultTaskListener<Boolean>((Activity) this.context) {
			@Override
			public void onSuccess(Boolean hasFollow) {
				if (hasFollow) {
					followImageView.setImageResource(R.drawable.following_avatar);
				} else {
					followImageView.setImageResource(R.drawable.follow_avatar);
				}

			}
		}).execute(user.getId());

		// 关注事件
		// 如果是自己隐藏按钮
		if (currUserId == user.getId()) {
			holder.follow.setVisibility(View.INVISIBLE);
		} else {
			holder.follow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new IsFollow((Activity) FollowsAdapter.this.context).setListener(
							new DefaultTaskListener<Boolean>((Activity) FollowsAdapter.this.context, "操作中...") {
								@Override
								public void onSuccess(Boolean hasFollow) {
									if (hasFollow) {
										new Unfollow((Activity) FollowsAdapter.this.context).setListener(
												new DefaultTaskListener<Boolean>(
														(Activity) FollowsAdapter.this.context, "正在反关注...") {
													@Override
													public void onSuccess(Boolean result) {
														if (result) {
															// holder.follow.setImageResource(R.drawable.follow_avatar);
															FollowsAdapter.this.notifyDataSetChanged();
														}
													}
												}).execute(user.getId());

									} else {
										new Follow((Activity) FollowsAdapter.this.context).setListener(
												new DefaultTaskListener<Boolean>(
														(Activity) FollowsAdapter.this.context, "正在关注...") {
													@Override
													public void onSuccess(Boolean result) {
														if (result) {
															// holder.follow.setImageResource(R.drawable.following_avatar);
															FollowsAdapter.this.notifyDataSetChanged();
														}
													}
												}).execute(user.getId());

									}

								}
							}).execute(user.getId());
				}
			});
		}

		return view;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public List<UserVO> getList() {
		return list;
	}

	public void setList(List<UserVO> list) {
		this.list = list;
	}

}
