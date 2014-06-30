package net.ipetty.android.discover;

import java.util.List;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.home.InitFeedListListener;
import net.ipetty.vo.FeedVO;
import android.app.Activity;

public class DiscoverListener extends DefaultTaskListener<List<FeedVO>> {
	private final static String TAG = InitFeedListListener.class.getSimpleName();
	private final DiscoverAdapter adapter;

	public DiscoverListener(Activity activity, DiscoverAdapter adapter) {
		super(activity);
		this.adapter = adapter;
	}

	@Override
	public void onCancelled(List<FeedVO> result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(List<FeedVO> result) {
		// TODO Auto-generated method stub
		adapter.loadDate(result);
	}

}
