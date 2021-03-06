/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.home;

import android.util.Log;
import java.util.List;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.MyPullToRefreshListView;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author yneos
 */
public class LoadMoreFeedListListener extends DefaultTaskListener<List<FeedVO>> {

	private final MainHomeFragment mainHomeFragment;
	private final MyPullToRefreshListView myPullToRefreshListView;

	// 加载更多
	public LoadMoreFeedListListener(MainHomeFragment mainHomeFragment, MyPullToRefreshListView myPullToRefreshListView) {
		super(mainHomeFragment.getActivity());
		this.mainHomeFragment = mainHomeFragment;
		this.myPullToRefreshListView = myPullToRefreshListView;
	}

	@Override
	public void onSuccess(List<FeedVO> result) {
		Log.d(TAG, "onSuccess");
		mainHomeFragment.loadMoreForResult(result);
	}

	@Override
	public void onError(Throwable ex) {
		Log.d(TAG, "onError:" + ex.getClass().getName());
		super.onError(ex);
		myPullToRefreshListView.hideMoreView();

	}

}
