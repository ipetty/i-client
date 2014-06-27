package net.ipetty.android.sdk.task.pet;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import android.app.Activity;

/**
 * UpdatePetAvatar
 * 
 * @author luocanfeng
 * @date 2014年6月27日
 */
public class UpdatePetAvatar extends Task<String, String> {

	public UpdatePetAvatar(Activity activity) {
		super(activity);
	}

	@Override
	protected String myDoInBackground(String... args) {
		return IpetApi.init(activity).getPetApi().updateAvatar(args[0], args[1]);
	}

}
