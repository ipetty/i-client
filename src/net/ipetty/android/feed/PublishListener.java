/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.feed;

import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.vo.FeedVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author yneos
 */
public class PublishListener extends DefaultTaskListener<FeedVO> {

	public PublishListener(Activity activity) {
		super(activity, "正在发布...");
	}

	@Override
	public void onSuccess(FeedVO result) {
		Log.d(TAG, "onSuccess");
		Intent intent = new Intent(Constant.BROADCAST_INTENT_FEED_PUBLISH);
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.FEEDVO_JSON_SERIALIZABLE, JSONUtils.toJson(result));
		intent.putExtras(mBundle);
		activity.sendBroadcast(intent);
		Log.d(TAG, "send publish");
		activity.finish();
	}

}
