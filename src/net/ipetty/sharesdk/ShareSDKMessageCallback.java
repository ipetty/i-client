package net.ipetty.sharesdk;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;

/**
 * ShareSDKMessageCallback
 * 
 * @author luocanfeng
 * @date 2014年7月21日
 */
public class ShareSDKMessageCallback implements Callback {

	private Context context;

	public ShareSDKMessageCallback(Context context) {
		super();
		this.context = context;
	}

	/* 处理异步消息 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case ShareSDKConstant.MSG_SHARE_COMPLETE:
			Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_SHARE_CANCEL:
			Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_SHARE_ERROR:
			Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_USERID_FOUND:
			// Toast.makeText(context, "用户信息已存在，正在跳转登录操作…",
			// Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_LOGIN:
			// Toast.makeText(context, "正在使用" + msg.obj + "的授权帐号登录" +
			// context.getString(R.string.app_name),
			// Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_AUTH_CANCEL:
			Toast.makeText(context, "授权操作已取消", Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_AUTH_ERROR:
			Toast.makeText(context, "授权操作出错", Toast.LENGTH_SHORT).show();
			break;
		case ShareSDKConstant.MSG_AUTH_COMPLETE:
			// Toast.makeText(context, "授权成功，正在跳转登录操作…",
			// Toast.LENGTH_SHORT).show();
			break;
		}
		return false;
	}

}
