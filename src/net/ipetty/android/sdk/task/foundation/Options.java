package net.ipetty.android.sdk.task.foundation;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;

/**
 * Options
 * 
 * @author luocanfeng
 * @date 2014年7月25日
 */
public class Options {

	public static final List<Option> HUMAN_GENDER;
	public static final List<Option> PET_FAMILY;
	public static final List<Option> PET_GENDER;

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

}
