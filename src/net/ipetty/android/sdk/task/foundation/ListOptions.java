package net.ipetty.android.sdk.task.foundation;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.android.core.Task;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import android.app.Activity;

public class ListOptions extends Task<String, List<Option>> {

	public ListOptions(Activity activity) {
		super(activity);
	}

	@Override
	protected List<Option> myDoInBackground(String... args) {
		String optionGroup = args[0];
		if (OptionGroup.HUMAN_GENDER.equals(optionGroup)) {
			return Options.HUMAN_GENDER;
		} else if (OptionGroup.PET_GENDER.equals(optionGroup)) {
			return Options.PET_GENDER;
		} else if (OptionGroup.PET_FAMILY.equals(optionGroup)) {
			return Options.PET_FAMILY;
		}
		// 存在问题
		return new ArrayList<Option>();
		// return
		// IpetApi.init(activity).getFoundationApi().listOptionsByGroup(args[0]);
	}

}
