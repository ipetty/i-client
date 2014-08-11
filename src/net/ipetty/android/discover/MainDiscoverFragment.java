package net.ipetty.android.discover;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BaseFragment;
import net.ipetty.android.core.util.NetWorkUtils;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainDiscoverFragment extends BaseFragment {

	private Activity activity;
	// private GridView gridview;
	// private DiscoverAdapter adapter;
	private DiscoverRender render;
	private Long lastTimeMillis;
	private final Integer pageSize = 21;

	private LayoutInflater mInflater;
	private RelativeLayout mMoreView;
	private TextView mText;
	protected ImageView mImage;
	protected ProgressBar mProgress;
	private LinearLayout content;

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
			loadData();
		}

	};

	@Override
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
		super.onViewResume();
		// loadData();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		this.activity = getActivity();
		render = new DiscoverRender(this.activity);
		content = (LinearLayout) this.getActivity().findViewById(R.id.content);

		initFooter(this.getActivity());
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

	private void initFooter(Context context) {
		mInflater = (LayoutInflater) LayoutInflater.from(context);
		mMoreView = (RelativeLayout) mInflater.inflate(R.layout.pull_to_refreshing, null, false);
		mImage = (ImageView) mMoreView.findViewById(R.id.pull_to_refresh_image);
		RotateAnimation mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setDuration(1200);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mImage.startAnimation(mRotateAnimation);
		content.addView(mMoreView);

	}

	public void showMoreView() {
		// TODO Auto-generated method stub
		mMoreView.setVisibility(View.VISIBLE);
	}

	public void hideMoreView() {
		// TODO Auto-generated method stub
		mMoreView.setVisibility(View.GONE);
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
				new DefaultTaskListener<List<FeedVO>>(MainDiscoverFragment.this.getActivity()) {
					@Override
					public void onSuccess(List<FeedVO> result) {
						// TODO Auto-generated method stub
						render.loadData(result);
						hideMoreView();
					}
				}).execute(getRefreshTime().toString(), "0", pageSize.toString());
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
