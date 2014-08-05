/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.feed;

import java.util.Date;
import java.util.List;

import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;
import android.app.Activity;
import android.util.Log;

/**
 * 
 * @author Administrator
 */
public class ListByTimelineForHomePage extends Task<String, List<FeedVO>> {

	public ListByTimelineForHomePage(Activity activity) {
		super(activity);
	}

	@Override
	protected List<FeedVO> myDoInBackground(String... args) {
		Log.d(TAG, "onSuccess");
		List<FeedVO> result = IpetApi
				.init(activity)
				.getFeedApi()
				.listByTimelineForHomePage(new Date(Long.valueOf(args[0])), Integer.valueOf(args[1]),
						Integer.valueOf(args[2]));
		for (FeedVO feedVO : result) {
			UserApiWithCache.getUserById4Synchronous(activity, feedVO.getCreatedBy());
			for (FeedFavorVO fvo : feedVO.getFavors()) {
				UserApiWithCache.getUserById4Synchronous(activity, fvo.getCreatedBy());
			}
			for (CommentVO cvo : feedVO.getComments()) {
				UserApiWithCache.getUserById4Synchronous(activity, cvo.getCreatedBy());
			}
		}
		return result;
	}
}
