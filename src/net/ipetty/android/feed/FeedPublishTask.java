/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.feed;

import android.app.Activity;
import java.io.File;
import net.ipetty.android.core.Task;
import net.ipetty.android.core.util.ImageUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author Administrator
 */
public class FeedPublishTask extends Task< FeedFormVO, FeedVO> {

	public FeedPublishTask(Activity activity) {
		super(activity);
	}

	@Override
	protected FeedVO myDoInBackground(FeedFormVO... args) {
		FeedFormVO ffvo = args[0];
		String srcFile = ffvo.getImagePath();
		String imageName = (System.currentTimeMillis() + ".jpg");
		String outPath = new File(PathUtils.getCarmerDir(), imageName).getPath();
		ImageUtils.compressImage(srcFile, outPath);
		ffvo.setImagePath(outPath);
		return IpetApi.init(activity).getFeedApi().publishWithLocation(ffvo);
	}

}
