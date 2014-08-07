package net.ipetty.android.fans;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.MyPullToRefreshListView;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.ListFollowers;
import net.ipetty.android.sdk.task.user.ListFriends;
import net.ipetty.vo.UserVO;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FansActivity extends BaseActivity {

	private FansAdapter adapter; // 定义适配器
	private MyPullToRefreshListView listView;

	private int userId;
	private int currUserId;
	private Boolean isCurrentUser;
	private int currentPage = 0;
	private int pageSize = 20;
	private Boolean hasMore = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fans);
		Log.d(TAG, "onCreate");
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_fans));
		btnBack.setOnClickListener(new BackClickListener(this));

		currUserId = IpetApi.init(this).getCurrUserId();
		this.userId = this.getIntent().getExtras().getInt(Constant.INTENT_USER_ID_KEY);
		isCurrentUser = userId == currUserId;

		listView = (MyPullToRefreshListView) this.findViewById(R.id.listView);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				initData(listView);
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (FansActivity.this.hasMore) {
					FansActivity.this.currentPage++;
					new ListFriends(FansActivity.this).setListener(
							new DefaultTaskListener<List<UserVO>>(FansActivity.this) {
								@Override
								public void onSuccess(List<UserVO> result) {
									if (result.size() < FansActivity.this.pageSize) {
										FansActivity.this.hasMore = false;
										listView.hideMoreView();
									} else {
										listView.showMoreView();
									}
									adapter.getList().addAll(result);
									adapter.notifyDataSetChanged();
								}
							}).execute(FansActivity.this.userId, FansActivity.this.currentPage,
							FansActivity.this.pageSize);
				}
			}
		});

		// 初始化适配器
		adapter = new FansAdapter(this, userId, currUserId);
		listView.setAdapter(adapter);
		initData(null);
	}

	// 初始化数据
	public void initData(final PullToRefreshListView view) {

		new ListFollowers(this).setListener(new DefaultTaskListener<List<UserVO>>(this) {
			@Override
			public void onSuccess(List<UserVO> result) {
				if (result.size() < FansActivity.this.pageSize) {
					FansActivity.this.hasMore = false;
					listView.hideMoreView();
				} else {
					listView.showMoreView();
				}
				adapter.setList(result);
				adapter.notifyDataSetChanged();
				if (null != view) {
					view.onRefreshComplete();
				}
			}
		}).execute(this.userId, 0, this.pageSize);
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

	}

	@Override
	public void onViewResume() {
		Log.d(TAG, "onViewResume");
		adapter.notifyDataSetChanged();
	}
}
