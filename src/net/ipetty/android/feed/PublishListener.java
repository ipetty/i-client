/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.feed;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author yneos
 */
public class PublishListener extends DefaultTaskListener<FeedVO> {

    private final static String TAG = PublishListener.class.getSimpleName();

    public PublishListener(Activity activity) {
        super(activity, "正在发布...");
    }

    @Override
    public void onSuccess(FeedVO result) {
        Log.d(TAG, "onSuccess");
        activity.finish();
    }

}
