/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.home;

import java.util.List;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedVO;
import android.util.Log;

/**
 * 
 * @author yneos
 */
public class LoadMoreFeedListListener extends DefaultTaskListener<List<FeedVO>> {

	private final MainHomeFragment mainHomeFragment;

	// 加载更多
	public LoadMoreFeedListListener(MainHomeFragment mainHomeFragment) {
		super(mainHomeFragment.getActivity());
		this.mainHomeFragment = mainHomeFragment;
	}

	@Override
	public void onSuccess(List<FeedVO> result) {
		Log.d(TAG, "onSuccess");
		mainHomeFragment.loadMoreForResult(result);
	}

}
