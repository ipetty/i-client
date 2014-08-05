package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import android.app.Activity;

public class CheckUserEmailAvailable extends Task<String, Boolean> {

	public CheckUserEmailAvailable(Activity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(String... args) {
		return IpetApi.init(activity).getUserApi().checkEmailAvailable(args[0]);
	}

}
