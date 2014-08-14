package net.ipetty.sharesdk.onekeyshare;

import net.ipetty.android.core.util.AppUtils;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 为各平台打造不同的分享内容
 * 
 * @author luocanfeng
 * @date 2014年8月13日
 */
public class OneKeyShareCustomizeCallback implements ShareContentCustomizeCallback {

	private static final String TAG = OneKeyShareCustomizeCallback.class.getSimpleName();

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	public void onShare(Platform platform, ShareParams paramsToShare) {
		Log.d(TAG, "onShare");
		Log.d(TAG, "paramsToShare.text=" + paramsToShare.getText());
		Log.d(TAG, "platform.name=" + platform.getName());
		if (Wechat.NAME.equals(platform.getName()) || WechatMoments.NAME.equals(platform.getName())) {
			// 微信与微信朋友圈，微信不支持分享网络图片

			// android.os.NetworkOnMainThreadException
			// String imageUrl = paramsToShare.getImageUrl();
			// if (StringUtils.isNotBlank(imageUrl)) {
			// Log.d(TAG, "imageUrl=" + imageUrl);
			// ImageLoader.getInstance().loadImageSync(imageUrl, options);
			// String imagePath =
			// Uri.fromFile(ImageLoader.getInstance().getDiskCache().get(imageUrl)).getPath();
			// Log.d(TAG, "imagePath=" + imagePath);
			// paramsToShare.setImagePath(imagePath);
			// paramsToShare.setImageUrl(null);
			// }

			// 分享图片，此时设置了text也无法显示
			paramsToShare.setShareType(Platform.SHARE_IMAGE);
			// paramsToShare.setText(null);

			// 分享网页，此时需要设置Url为查看具体消息的网页
			// paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
			// paramsToShare.setUrl("http://ipetty.net/");
			// } else if (QQ.NAME.equals(platform.getName())) {
			// QQ
			// title, titleUrl, text, imageUrl/imagePath
			// paramsToShare.setImagePath(null);
			// } else if (QZone.NAME.equals(platform.getName())) {
			// QQ空间，分享图文
			// title, titleUrl, text, imageUrl, site, siteUrl
			// paramsToShare.setImagePath(null);
		} else if (SinaWeibo.NAME.equals(platform.getName())) {
			// 新浪微博，分享图文
			// text, imageUrl/imagePath, latitude, longitude
			paramsToShare.setImagePath(null);
		}
	}

}
