/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.home;

import java.util.List;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedVO;
import android.app.Activity;
import android.util.Log;

/**
 * 
 * @author yneos
 */
public class InitFeedListListener extends DefaultTaskListener<List<FeedVO>> {

	private final FeedAdapter adapter;

	public InitFeedListListener(Activity activity, FeedAdapter adapter) {
		super(activity, "努力加载中...");
		this.adapter = adapter;
	}

	@Override
	public void onSuccess(List<FeedVO> result) {
		Log.d(TAG, "onSuccess");

		adapter.getList().clear();
		adapter.getList().addAll(result);
		adapter.notifyDataSetChanged();
	}

}
