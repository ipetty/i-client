/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.home;

import android.app.Activity;
import android.util.Log;
import java.util.List;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author yneos
 */
public class InitFeedListListener extends DefaultTaskListener<List<FeedVO>> {

	private final static String TAG = InitFeedListListener.class.getSimpleName();
	private final FeedAdapter adapter;

	public InitFeedListListener(Activity activity, FeedAdapter adapter) {
		super(activity, "努力为您加载...");
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
