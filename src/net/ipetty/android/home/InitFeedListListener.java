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
public class InitFeedListListener extends DefaultTaskListener<List<FeedVO>> {

	private final MainHomeFragment mainHomeFragment;
	private FeedAdapter adapter;

	public InitFeedListListener(MainHomeFragment mainHomeFragment, FeedAdapter adapter) {
		super(mainHomeFragment.getActivity());
		this.mainHomeFragment = mainHomeFragment;
		this.adapter = adapter;
	}

	@Override
	public void onSuccess(List<FeedVO> result) {
		Log.d(TAG, "onSuccess");
		adapter.getList().clear();
		mainHomeFragment.loadMoreForResult(result);
	}

}
