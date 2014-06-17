package net.ipetty.android.sdk.task.foundation;

import java.util.List;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.Option;
import android.app.Activity;

public class ListOptions extends Task<String, List<Option>> {

	public ListOptions(Activity activity) {
		super(activity);
	}

	@Override
	protected List<Option> myDoInBackground(String... args) {
		return IpetApi.init(activity).getFoundationApi().listOptionsByGroup(args[0]);
	}

}
