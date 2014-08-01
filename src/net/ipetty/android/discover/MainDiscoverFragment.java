package net.ipetty.android.discover;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BaseFragment;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.feed.SimpleFeedActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.task.feed.ListByTimelineForSquare;
import net.ipetty.vo.FeedVO;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainDiscoverFragment extends BaseFragment {

	public final static String TAG = MainDiscoverFragment.class.getSimpleName();
	private Activity activity;
	private GridView gridview;
	private DiscoverAdapter adapter;
	private Long lastTimeMillis;
	private final Integer pageSize = 20;

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
			loadData();
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		this.activity = getActivity();
		gridview = (GridView) this.activity.findViewById(R.id.gridview);
		adapter = new DiscoverAdapter(this.activity);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent((MainActivity) getActivity(), SimpleFeedActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, id);
				FeedVO feedVO = (FeedVO) adapter.getItem(position);
				intent.putExtra(Constant.FEEDVO_JSON_SERIALIZABLE, JSONUtils.toJson(feedVO).toString());
				((MainActivity) getActivity()).startActivity(intent);
			}
		});
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		super.onViewReady(savedInstanceState);

		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		new ListByTimelineForSquare(MainDiscoverFragment.this.getActivity()).setListener(
				new DiscoverListener(MainDiscoverFragment.this.getActivity(), adapter)).execute(
				getRefreshTime().toString(), "0", pageSize.toString());
	}

	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this.getActivity())) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Discover(this.getActivity(), this.lastTimeMillis);
			return this.lastTimeMillis;
		}
		return MyAppStateManager.getLastRefrsh4Discover(this.getActivity());
	}

}
