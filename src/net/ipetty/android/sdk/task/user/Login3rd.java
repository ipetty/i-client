package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * 使用第三方帐号登陆
 * 
 * @author luocanfeng
 * @date 2014年7月22日
 */
public class Login3rd extends Task<String, UserVO> {

	public Login3rd(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(String... args) {
		Log.d(TAG, "myDoInBackground");
		String platform = args[0];
		String userId = args[1];
		return IpetApi.init(activity).getUserApi().login3rd(platform, userId);
	}

}
