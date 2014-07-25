package net.ipetty.android.sdk.task.foundation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.ipetty.android.core.Task;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import android.app.Activity;

public class GetOptionValueLabelMap extends Task<String, Map<String, String>> {

	public GetOptionValueLabelMap(Activity activity) {
		super(activity);
	}

	@Override
	protected Map<String, String> myDoInBackground(String... args) {
		String optionGroup = args[0];
		List<Option> options;
		if (OptionGroup.HUMAN_GENDER.equals(optionGroup)) {
			options = Options.HUMAN_GENDER;
		} else if (OptionGroup.PET_GENDER.equals(optionGroup)) {
			options = Options.PET_GENDER;
		} else if (OptionGroup.PET_FAMILY.equals(optionGroup)) {
			options = Options.PET_FAMILY;
		} else {
			options = new ArrayList<Option>();
		}

		Map<String, String> valueTextMap = new LinkedHashMap<String, String>();
		for (Option option : options) {
			valueTextMap.put(option.getValue(), option.getLabel());
		}
		return valueTextMap;
	}

}
