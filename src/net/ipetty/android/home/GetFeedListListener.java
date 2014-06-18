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
public class GetFeedListListener extends DefaultTaskListener<List<FeedVO>> {

    private final static String TAG = GetFeedListListener.class.getSimpleName();
    private final FeedAdapter ap;
    private final PullToRefreshListView prv;
    private final Boolean isRefresh;

    public GetFeedListListener(Activity activity, FeedAdapter adapter) {
        super(activity, "加载中...");
        ap = adapter;
        prv = null;
        isRefresh = false;
    }

    public GetFeedListListener(Activity activity, FeedAdapter adapter, PullToRefreshListView pullToRefreshListView, Boolean isRefresh) {
        super(activity);
        this.ap = adapter;
        this.prv = pullToRefreshListView;
        this.isRefresh = isRefresh;
    }

    @Override
    public void onSuccess(List<FeedVO> result) {
        Log.d(TAG, "onSuccess");
        if (isRefresh) {
            ap.getList().clear();
        }
        ap.getList().addAll(result);
        ap.notifyDataSetChanged();
        if (null != prv) {
            prv.onRefreshComplete();
        }
    }

}
