package net.ipetty.android.setting;

import android.app.Activity;
import net.ipetty.android.core.ActivityManager;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginHasAccountActivity;

/**
 * LogoutTaskListener
 *
 * @author luocanfeng
 * @date 2014年6月17日
 */
public class LogoutTaskListener extends DefaultTaskListener<Void> {

	public LogoutTaskListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onSuccess(Void result) {
		ActivityManager.getInstance().finish();
		AppUtils.goTo(activity, LoginHasAccountActivity.class);
		//activity.finish();
	}

}
