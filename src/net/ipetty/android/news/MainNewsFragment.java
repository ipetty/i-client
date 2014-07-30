package net.ipetty.android.news;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.activity.ListActivities;
import net.ipetty.android.sdk.task.user.ListFollowers;
import net.ipetty.vo.ActivityVO;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainNewsFragment extends Fragment {

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
	private final Integer activitiePageSize = 5;
	private Boolean activitieHasMore = true;

	private Integer followerPageNumber = 0;
	private final Integer followerPageSize = 5;
	private Boolean followerHasMore = true;

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

		loadData();
	}

	private void loadData() {
		activitieHasMore = true;
		followerHasMore = true;
		activitiePageNumber = 0;
		followerPageNumber = 0;

		List<ActivityVO> arr = new ArrayList<ActivityVO>();

		ActivityVO act = new ActivityVO();
		ActivityVO act1 = new ActivityVO();
		ActivityVO act2 = new ActivityVO();
		act.setId(1L);
		act1.setId(2L);
		act2.setId(3L);
		act.setType(Constant.NEWS_TYPE_FAVOR);
		act1.setType(Constant.NEWS_TYPE_FOLLOWED);
		act2.setType(Constant.NEWS_TYPE_COMMENT);
		arr.add(act);
		arr.add(act);
		arr.add(act2);
		arr.add(act1);
		arr.add(act1);
		arr.add(act2);
		arr.add(act1);
		arr.add(act);
		related_me_adapter.setList(arr);
		related_me_adapter.notifyDataSetChanged();

		if (false) {
			new ListActivities(this.getActivity()).setListener(
					new DefaultTaskListener<List<ActivityVO>>(MainNewsFragment.this) {
						@Override
						public void onSuccess(List<ActivityVO> result) {
							related_me_adapter.setList(result);
							related_me_adapter.notifyDataSetChanged();
						}
					}).execute(activitiePageNumber, this.activitiePageSize);
		}
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

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		// loadData();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

}
