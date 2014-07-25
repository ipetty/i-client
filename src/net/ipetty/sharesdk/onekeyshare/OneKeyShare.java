package net.ipetty.sharesdk.onekeyshare;

import java.util.HashMap;

import net.ipetty.R;
import net.ipetty.sharesdk.ShareSDKConstant;
import net.ipetty.sharesdk.ShareSDKMessageCallback;
import android.app.Activity;
import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 一键分享
 * 
 * @author luocanfeng
 * @date 2014年7月25日
 */
public class OneKeyShare {

	protected String TAG = getClass().getSimpleName();

	protected Activity activity;
	protected ShareSDKMessageCallback shareSDKMessageCallback;

	public OneKeyShare(Activity activity) {
		super();
		this.activity = activity;
		shareSDKMessageCallback = new ShareSDKMessageCallback(this.activity);
	}

	/**
	 * 一键分享
	 */
	public void oneKeyShare(Context context, String title, String text, String imagePath, boolean silent) {
		OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
		oks.setTitle(title);
		oks.setText(text);
		oks.setImagePath(imagePath);
		oks.setSilent(silent); // 是否直接分享，true则直接分享
		oks.setCallback(new ShareListener());
		oks.show(context);
	}

	/**
	 * 分享到第三方平台操作的Listener
	 */
	public class ShareListener implements PlatformActionListener {
		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> args) {
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_COMPLETE, shareSDKMessageCallback);
		}

		@Override
		public void onCancel(Platform platform, int action) {
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_CANCEL, shareSDKMessageCallback);
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			t.printStackTrace();
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_ERROR, shareSDKMessageCallback);
		}
	}

}
