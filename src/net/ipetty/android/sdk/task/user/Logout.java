package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import android.app.Activity;
import android.util.Log;

/**
 * Logout
 * 
 * @author luocanfeng
 * @date 2014年6月17日
 */
public class Logout extends Task<Void, Void> {

	public final static String TAG = Logout.class.getSimpleName();

	public Logout(Activity activity) {
		super(activity);
	}

	@Override
	protected Void myDoInBackground(Void... args) {
		Log.d(TAG, "logout");
		IpetApi.init(activity).getUserApi().logout();
		return null;
	}

}
