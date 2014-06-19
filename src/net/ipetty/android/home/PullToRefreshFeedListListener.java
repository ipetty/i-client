/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.home;

import android.app.Activity;
import android.util.Log;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.util.List;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author yneos
 */
public class PullToRefreshFeedListListener extends DefaultTaskListener<List<FeedVO>> {

    private final static String TAG = PullToRefreshFeedListListener.class.getSimpleName();
    private final FeedAdapter adapter;
    private final PullToRefreshListView pullToRefreshListView;

    //下接刷新
    public PullToRefreshFeedListListener(Activity activity, FeedAdapter adapter, PullToRefreshListView pullToRefreshListView) {
        super(activity, "正在刷新...");
        this.adapter = adapter;
        this.pullToRefreshListView = pullToRefreshListView;
    }

    @Override
    public void onSuccess(List<FeedVO> result) {
        Log.d(TAG, "onSuccess:" + result.size());
        adapter.getList().clear();
        adapter.getList().addAll(result);
        adapter.notifyDataSetChanged();
        if (null != pullToRefreshListView) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

}
