package net.ipetty.android.like;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.task.feed.GetFeedById;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.vo.FeedVO;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class LikeActivity extends BaseActivity {

	private LikeAdapter adapter; // 定义适配器
	private PullToRefreshListView listView;
	private Long feedId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_like);
		Log.d(TAG, "onCreate");

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		feedId = this.getIntent().getExtras().getLong(Constant.INTENT_FEED_ID_KEY);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_like));
		btnBack.setOnClickListener(new BackClickListener(this));

		listView = (PullToRefreshListView) this.findViewById(R.id.listView);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetFeedById(LikeActivity.this).setListener(
						new DefaultTaskListener<FeedVO>(LikeActivity.this, "刷新中") {
							@Override
							public void onSuccess(FeedVO result) {
								adapter.setList(result.getFavors());
								adapter.notifyDataSetChanged();
								listView.onRefreshComplete();
							}
						}).execute(feedId);

			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// 为分页预留
			}
		});

		// 点击查看某个个人主页
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				long userId = id;
				Log.d(TAG, "id->" + id);
				Intent intent = new Intent(LikeActivity.this, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, (int) userId);
				startActivity(intent);
			}
		});

		// 初始化适配器
		adapter = new LikeAdapter(this);
		listView.setAdapter(adapter);
		loadData();
	}

	// 加载数据
	public void loadData() {
		new GetFeedById(this).setListener(new DefaultTaskListener<FeedVO>(this, "加载中") {
			@Override
			public void onSuccess(FeedVO result) {
				adapter.setList(result.getFavors());
				adapter.notifyDataSetChanged();
			}
		}).execute(feedId);
	}
}
