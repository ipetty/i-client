package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * 使用第三方帐号登陆或注册后登陆返回
 * 
 * @author luocanfeng
 * @date 2014年7月23日
 */
public class LoginOrRegister3rd extends Task<String, UserVO> {

	public final static String TAG = LoginOrRegister3rd.class.getSimpleName();

	public LoginOrRegister3rd(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(String... args) {
		Log.d(TAG, "myDoInBackground");
		String platform = args[0];
		String userId = args[1];
		String email = args[2];
		String userName = args[3];
		return IpetApi.init(activity).getUserApi().loginOrRegister3rd(platform, userId, email, userName);
	}

}
