package net.ipetty.sharesdk.onekeyshare;

import java.util.HashMap;
import java.util.Random;

import net.ipetty.R;
import net.ipetty.sharesdk.ShareSDKConstant;
import net.ipetty.sharesdk.ShareSDKMessageCallback;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * 一键分享
 * 
 * @author luocanfeng
 * @date 2014年7月25日
 */
public class OneKeyShare {

	private static final String TAG = OneKeyShare.class.getSimpleName();

	private static final String SHARE_TITLE = "来自爱宠的分享";
	private static final String SHARE_SITE = "爱宠";
	private static final String SHARE_SITE_URL = "http://ipetty.net/";

	private static final String[] SHARE_QUOTES = { "亲，一大波汪星人&喵星人正在来袭，您还在等什么呢~", "亲，一大波汪星人&喵星人正在来袭，您还在等什么呢~",
			"亲，一大波汪星人&喵星人正在来袭，您还在等什么呢~", "一大波波汪星人&喵星人正在入侵爱宠，快去看看吧！", "一大波波汪星人&喵星人正在入侵爱宠，快去看看吧！" };

	protected Context context;
	protected ShareSDKMessageCallback shareSDKMessageCallback;
	private PlatformActionListener shareCallback = new ShareListener();
	private ShareContentCustomizeCallback customizeShareCallback = new OneKeyShareCustomizeCallback();

	public OneKeyShare(Context context) {
		super();
		this.context = context;
		this.shareSDKMessageCallback = new ShareSDKMessageCallback(this.context);
	}

	/**
	 * 分享消息
	 */
	public void share(String feedAuthor, String feedBody, String imageUrl) {
		ShareSDK.initSDK(this.context);
		OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, this.context.getString(R.string.app_name));
		oks.setTitle(SHARE_TITLE);
		oks.setTitleUrl(SHARE_SITE_URL);
		String quote = getQuote();
		oks.setText(StringUtils.isNotBlank(feedBody) ? quote + "// " + feedAuthor + "：" + feedBody : quote);
		oks.setImageUrl(imageUrl);
		oks.setSilent(false); // 是否直接分享，true则直接分享
		oks.setCallback(shareCallback);
		oks.setSite(SHARE_SITE);
		oks.setSiteUrl(SHARE_SITE_URL);
		oks.setShareContentCustomizeCallback(customizeShareCallback);
		oks.show(context);
	}

	private static String getQuote() {
		return SHARE_QUOTES[new Random().nextInt(SHARE_QUOTES.length)];
	}

	/**
	 * 分享到第三方平台操作的Listener
	 */
	public class ShareListener implements PlatformActionListener {
		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> args) {
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_COMPLETE, OneKeyShare.this.shareSDKMessageCallback);
		}

		@Override
		public void onCancel(Platform platform, int action) {
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_CANCEL, OneKeyShare.this.shareSDKMessageCallback);
		}

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			// t.printStackTrace();
			Log.d(TAG, "分享失败", t);
			UIHandler.sendEmptyMessage(ShareSDKConstant.MSG_SHARE_ERROR, OneKeyShare.this.shareSDKMessageCallback);
		}
	}

}
