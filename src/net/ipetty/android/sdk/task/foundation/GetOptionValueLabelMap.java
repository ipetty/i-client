package net.ipetty.android.sdk.task.foundation;

import java.util.Map;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import android.app.Activity;

public class GetOptionValueLabelMap extends Task<String, Map<String, String>> {

	public GetOptionValueLabelMap(Activity activity) {
		super(activity);
	}

	@Override
	protected Map<String, String> myDoInBackground(String... args) {
		return IpetApi.init(activity).getFoundationApi().getOptionValueLabelMapByGroup(args[0]);
	}

}
