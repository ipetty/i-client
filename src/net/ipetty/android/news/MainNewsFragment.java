package net.ipetty.android.news;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BaseFragment;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.activity.ListActivities;
import net.ipetty.android.sdk.task.user.ListFollowers;
import net.ipetty.vo.ActivityVO;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainNewsFragment extends BaseFragment {

	public final static String TAG = MainNewsFragment.class.getSimpleName();
	private Activity activity;
	private ViewFlipper viewFlipper;

	private View related_me;
	private View my_follows;

	private PullToRefreshListView related_me_listView;
	private RelatedMeAdapter related_me_adapter;

	private PullToRefreshListView my_follows_listView;
	private MyFollowsAdapter my_follows_adapter;

	private Integer activitiePageNumber = 0;
	private final Integer activitiePageSize = 20;
	private Boolean activitieHasMore = false;

	private Integer followerPageNumber = 0;
	private final Integer followerPageSize = 20;
	private Boolean followerHasMore = true;

	private Long lastTimeMillis;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_news, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		super.onViewReady(savedInstanceState);

		this.activity = this.getActivity();

		viewFlipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		related_me = activity.findViewById(R.id.related_me);
		my_follows = activity.findViewById(R.id.my_follows);
		related_me.setOnClickListener(new TabClickListener(0));
		my_follows.setOnClickListener(new TabClickListener(1));

		related_me_listView = (PullToRefreshListView) activity.findViewById(R.id.related_me_listView);
		// related_me_listView.setMode(Mode.PULL_FROM_END);

		related_me_adapter = new RelatedMeAdapter(this.getActivity());
		related_me_listView.setAdapter(related_me_adapter);

		related_me_listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				((MainActivity) MainNewsFragment.this.getActivity()).hideNewsDot();
				String label = DateUtils.formatDateTime(MainNewsFragment.this.getActivity().getApplicationContext(),
						getRefreshTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 刷新数据
				new ListActivities(MainNewsFragment.this.getActivity()).setListener(
						new DefaultTaskListener<List<ActivityVO>>(MainNewsFragment.this) {
							@Override
							public void onSuccess(List<ActivityVO> result) {
								related_me_adapter.getList().clear();
								related_me_adapter.setList(result);
								related_me_adapter.notifyDataSetChanged();
								related_me_listView.onRefreshComplete();

							}
						}).execute(0, MainNewsFragment.this.activitiePageSize);
				// 重置页号
				activitieHasMore = false;
				activitiePageNumber = 0;
			}
		});
		if (false) {
			related_me_listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
				@Override
				public void onLastItemVisible() {
					// 加载更多
					if (activitieHasMore) {
						new ListActivities(MainNewsFragment.this.getActivity()).setListener(
								new DefaultTaskListener<List<ActivityVO>>(MainNewsFragment.this, "加载中...") {
									@Override
									public void onSuccess(List<ActivityVO> result) {
										if (result.size() < activitiePageSize) {
											activitieHasMore = false;
										}
										if (result.size() > 0) {
											related_me_adapter.getList().addAll(result);
											related_me_adapter.notifyDataSetChanged();
										}
									}
								}).execute(++activitiePageNumber, activitiePageSize);

					}
				}
			});

			my_follows_listView = (PullToRefreshListView) activity.findViewById(R.id.my_follows_listView);
			my_follows_listView.setMode(Mode.PULL_FROM_END);
			my_follows_adapter = new MyFollowsAdapter(this.getActivity());
			my_follows_listView.setAdapter(my_follows_adapter);

			my_follows_listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
				@Override
				public void onLastItemVisible() {
					// 加载更多
					if (followerHasMore) {
						new ListFollowers(MainNewsFragment.this.getActivity()).setListener(
								new DefaultTaskListener<List<UserVO>>(MainNewsFragment.this) {
									@Override
									public void onSuccess(List<UserVO> result) {
										if (result.size() < followerPageSize) {
											followerHasMore = false;
											return;
										}
										if (result.size() > 0) {
											my_follows_adapter.getList().addAll(result);
											my_follows_adapter.notifyDataSetChanged();
										}
									}
								}).execute(IpetApi.init(MainNewsFragment.this.getActivity()).getCurrUserId(),
								++followerPageNumber, followerPageSize);

					}
				}
			});
		}
		loadData();
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this.getActivity())) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(this.getActivity(), this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(this.getActivity());
	}

	private void loadData() {
		activitieHasMore = false;
		followerHasMore = true;
		activitiePageNumber = 0;
		followerPageNumber = 0;

		new ListActivities(this.getActivity()).setListener(
				new DefaultTaskListener<List<ActivityVO>>(MainNewsFragment.this) {
					@Override
					public void onSuccess(List<ActivityVO> result) {
						related_me_adapter.setList(result);
						related_me_adapter.notifyDataSetChanged();
					}
				}).execute(0, this.activitiePageSize);

		if (false) {
			new ListFollowers(this.getActivity()).setListener(
					new DefaultTaskListener<List<UserVO>>(MainNewsFragment.this) {
						@Override
						public void onSuccess(List<UserVO> users) {
							my_follows_adapter.setList(users);
							my_follows_adapter.notifyDataSetChanged();
						}
					}).execute(IpetApi.init(this.getActivity()).getCurrUserId(), followerPageNumber, followerPageSize);
		}
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
			if (index == 0) {
				related_me.setBackgroundResource(R.drawable.news_tab_selected);
				my_follows.setBackgroundResource(R.drawable.trans);
			} else {
				related_me.setBackgroundResource(R.drawable.trans);
				my_follows.setBackgroundResource(R.drawable.news_tab_selected);
			}
		}
	}

}
