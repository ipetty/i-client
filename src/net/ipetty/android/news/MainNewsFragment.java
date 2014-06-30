package net.ipetty.android.news;

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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.activity.ListActivities;
import net.ipetty.android.sdk.task.user.ListFollowers;
import net.ipetty.vo.ActivityVO;
import net.ipetty.vo.UserVO;

public class MainNewsFragment extends Fragment {

	public final static String TAG = "MainNewsFragment";
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

	private Integer followerPageNumber = 0;
	private final Integer followerPageSize = 5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_news, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated");
		this.activity = this.getActivity();

		viewFlipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		related_me = activity.findViewById(R.id.related_me);
		my_follows = activity.findViewById(R.id.my_follows);
		related_me.setOnClickListener(new TabClickListener(0));
		my_follows.setOnClickListener(new TabClickListener(1));

		related_me_listView = (PullToRefreshListView) activity.findViewById(R.id.related_me_listView);
		related_me_listView.setMode(Mode.PULL_FROM_END);

		related_me_adapter = new RelatedMeAdapter(this.getActivity());
		related_me_listView.setAdapter(related_me_adapter);

		my_follows_listView = (PullToRefreshListView) activity.findViewById(R.id.my_follows_listView);
		my_follows_listView.setMode(Mode.PULL_FROM_END);
		my_follows_adapter = new MyFollowsAdapter(this.getActivity());
		my_follows_listView.setAdapter(my_follows_adapter);
	}

	private void loadData() {
		new ListActivities(this.getActivity())
				.setListener(new DefaultTaskListener<List<ActivityVO>>(MainNewsFragment.this, "加载中...") {
					@Override
					public void onSuccess(List<ActivityVO> result) {
						related_me_adapter.setList(result);
						related_me_adapter.notifyDataSetChanged();
					}
				})
				.execute(this.activitiePageNumber, this.activitiePageSize);
		new ListFollowers(this.getActivity())
				.setListener(new DefaultTaskListener<List<UserVO>>(MainNewsFragment.this, "加载中...") {
					@Override
					public void onSuccess(List<UserVO> users) {
						my_follows_adapter.setList(users);
						my_follows_adapter.notifyDataSetChanged();
					}
				})
				.execute(IpetApi.init(this.getActivity()).getCurrUserId(), followerPageNumber, followerPageSize);
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
		Log.i(TAG, "onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
		loadData();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

}
