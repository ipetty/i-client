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

    private Boolean hasMore;

    //刷新
    public GetFeedListListener(Activity activity, FeedAdapter adapter) {
        super(activity, "加载中...");
        ap = adapter;
        prv = null;
        isRefresh = false;
    }

    //下接刷新
    public GetFeedListListener(Activity activity, FeedAdapter adapter, PullToRefreshListView pullToRefreshListView) {
        super(activity, "正在刷新...");
        this.ap = adapter;
        this.prv = pullToRefreshListView;
        this.isRefresh = true;
    }

    //加载更多
    public GetFeedListListener(Activity activity, FeedAdapter adapter, PullToRefreshListView pullToRefreshListView, Boolean hasMore) {
        super(activity, "加载更多...");
        this.ap = adapter;
        this.prv = pullToRefreshListView;
        this.isRefresh = false;
        this.hasMore = hasMore;
    }

    @Override
    public void onSuccess(List<FeedVO> result) {
        Log.d(TAG, "onSuccess");
        if (isRefresh) {
            ap.getList().clear();
        } else {
            if (result.size() > 0 && hasMore != null) {
                hasMore = false;
            }
        }
        ap.getList().addAll(result);
        ap.notifyDataSetChanged();
        if (null != prv) {
            prv.onRefreshComplete();
        }
    }

}
