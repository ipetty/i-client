package net.ipetty.android.login;

import net.ipetty.android.core.MyAsyncTask;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.ActivityUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;
import android.util.Log;

public class LoginTask extends MyAsyncTask<UserVO> {
	

	public final static String TAG = "LoginTask";

	public LoginTask(BaseActivity activity) {
		super(activity);
	}


	@Override
	protected UserVO doInBackground(String...args ) {
		String loginName = args[0];
		String password = args[1];
		
		IpetApi api = IpetApi.init(activity);
		return api.getUserApi().login(loginName, password);
	}

	@Override
	protected void onPostExecute(UserVO user) {
		super.onPostExecute(user);
		this.goMain();
	}

	// 转向主界面
	public void goMain() {
		Log.i(TAG, "to MainActivity");
		AppUtils.goTo(activity, MainActivity.class);
		ActivityUtils.getInstance().finish();
	}


}
