package net.ipetty.android.sdk.task.foundation;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.android.core.Task;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import android.app.Activity;

public class ListOptions extends Task<String, List<Option>> {

	private static final List<Option> HUMAN_GENDER;
	private static final List<Option> PET_FAMILY;
	private static final List<Option> PET_GENDER;

	static {
		HUMAN_GENDER = new ArrayList<Option>();
		HUMAN_GENDER.add(new Option(OptionGroup.HUMAN_GENDER, "female", "妹妹"));
		HUMAN_GENDER.add(new Option(OptionGroup.HUMAN_GENDER, "male", "哥哥"));
		HUMAN_GENDER.add(new Option(OptionGroup.HUMAN_GENDER, "other", "其他"));

		PET_FAMILY = new ArrayList<Option>();
		PET_FAMILY.add(new Option(OptionGroup.PET_FAMILY, "dog", "汪星人"));
		PET_FAMILY.add(new Option(OptionGroup.PET_FAMILY, "cat", "喵星人"));
		PET_FAMILY.add(new Option(OptionGroup.PET_FAMILY, "other", "异星人"));

		PET_GENDER = new ArrayList<Option>();
		PET_GENDER.add(new Option(OptionGroup.PET_GENDER, "female", "萌妹妹"));
		PET_GENDER.add(new Option(OptionGroup.PET_GENDER, "male", "帅哥哥"));
		PET_GENDER.add(new Option(OptionGroup.PET_GENDER, "other", "其他"));
	}

	public ListOptions(Activity activity) {
		super(activity);
	}

	@Override
	protected List<Option> myDoInBackground(String... args) {
		String optionGroup = args[0];
		if (OptionGroup.HUMAN_GENDER.equals(optionGroup)) {
			return HUMAN_GENDER;
		} else if (OptionGroup.PET_GENDER.equals(optionGroup)) {
			return PET_GENDER;
		} else if (OptionGroup.PET_FAMILY.equals(optionGroup)) {
			return PET_FAMILY;
		}
		// 存在问题
		return new ArrayList<Option>();
		// return
		// IpetApi.init(activity).getFoundationApi().listOptionsByGroup(args[0]);
	}

}
