/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.feed;

import android.app.Activity;
import java.util.Date;
import java.util.List;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author Administrator
 */
public class ListByTimelineForSpace extends Task<String, List<FeedVO>> {

	public ListByTimelineForSpace(Activity activity) {
		super(activity);
	}

	@Override
	protected List<FeedVO> myDoInBackground(String... args) {
		return IpetApi.init(activity).
				getFeedApi().
				listByTimelineForSpace(Integer.valueOf(args[0]),
						new Date(Long.valueOf(args[1])),
						Integer.valueOf(args[2]),
						Integer.valueOf(args[3]));
	}

}
