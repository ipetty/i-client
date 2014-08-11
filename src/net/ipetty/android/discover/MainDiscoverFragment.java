package net.ipetty.android.discover;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BaseFragment;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.sdk.task.feed.ListByTimelineForSquare;
import net.ipetty.vo.FeedVO;

public class MainDiscoverFragment extends BaseFragment {

	private Activity activity;
	// private GridView gridview;
	// private DiscoverAdapter adapter;
	private DiscoverRender render;
	private Long lastTimeMillis;
	private final Integer pageSize = 21;

	// private int width;
	// private List<FeedVO> list = new ArrayList<FeedVO>(0); // 这个就本地dataStore
	// DisplayImageOptions options;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_discover, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROADCAST_INTENT_FEED_PUBLISH);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_DELETE);
		this.getActivity().registerReceiver(broadcastreciver, filter);
		Log.d(TAG, "onCreate");
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "BroadcastReceiver:onReceive");
			loadData();
		}

	};

	@Override
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
		super.onViewResume();
		loadData();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		this.activity = getActivity();
		render = new DiscoverRender(this.activity);

		/*
		 * gridview = (GridView) this.activity.findViewById(R.id.gridview);
		 * adapter = new DiscoverAdapter(this.activity);
		 * gridview.setAdapter(adapter); gridview.setOnItemClickListener(new
		 * OnItemClickListener() {
		 *
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { // TODO Auto-generated method stub Intent
		 * intent = new Intent((MainActivity) getActivity(),
		 * SimpleFeedActivity.class);
		 * intent.putExtra(Constant.INTENT_FEED_ID_KEY, id); FeedVO feedVO =
		 * (FeedVO) adapter.getItem(position);
		 * intent.putExtra(Constant.FEEDVO_JSON_SERIALIZABLE,
		 * JSONUtils.toJson(feedVO).toString()); ((MainActivity)
		 * getActivity()).startActivity(intent); } });
		 */
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		super.onViewReady(savedInstanceState);
		loadData();
	}

	private void loadData() {
		Log.d(TAG, "loadData");
		new ListByTimelineForSquare(MainDiscoverFragment.this.getActivity()).setListener(
				new DefaultTaskListener<List<FeedVO>>(MainDiscoverFragment.this.getActivity()) {
					@Override
					public void onSuccess(List<FeedVO> result) {
						// TODO Auto-generated method stub
						render.loadData(result);
					}
				}).execute(getRefreshTime().toString(), "0", pageSize.toString());
	}

	private Long getRefreshTime() {
		Log.d(TAG, "getRefreshTime");
		if (NetWorkUtils.isNetworkConnected(this.getActivity())) {
			this.lastTimeMillis = System.currentTimeMillis() + Constant.TIME_CORRECT;
			MyAppStateManager.setLastRefrsh4Discover(this.getActivity(), this.lastTimeMillis);
			Log.d(TAG, "getRefreshTime:" + this.lastTimeMillis);
			return this.lastTimeMillis;
		}
		Long lastRefresh = MyAppStateManager.getLastRefrsh4Discover(this.getActivity());
		Log.d(TAG, "getRefreshTime:" + lastRefresh.toString());
		return lastRefresh;
	}

}
