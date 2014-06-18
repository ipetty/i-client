package net.ipetty.android.register;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ListOptionsTaskListener extends DefaultTaskListener<List<Option>> {

	private RegisterActivity registerActivity;
	private String optionGroup;
	private TextView textView;
	private Dialog dialog;
	private List<ModDialogItem> dialogItems;

	public ListOptionsTaskListener(Activity activity, String optionGroup) {
		super(activity);
		this.optionGroup = optionGroup;
	}

	@Override
	public void onSuccess(List<Option> result) {
		registerActivity = (RegisterActivity) super.activity;

		if (OptionGroup.PET_GENDER.equals(optionGroup)) {
			textView = registerActivity.getPetGenderText();
			dialog = registerActivity.getPetGenderDialog();
			dialogItems = registerActivity.getPetGenderItems();
		} else if (OptionGroup.PET_FAMILY.equals(optionGroup)) {
			textView = registerActivity.getPetFamilyText();
			dialog = registerActivity.getPetFamilyDialog();
			dialogItems = registerActivity.getPetFamilyItems();
		}

		for (Option option : result) {
			dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
		}

		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog = DialogUtils.modPopupDialog(registerActivity, dialogItems, dialog);
			}
		});
	}

	private OnClickListener dialogClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
			if (OptionGroup.PET_GENDER.equals(optionGroup)) {
				registerActivity.getPetGenderText().setText(label);
				registerActivity.setPetGender(value);
			} else if (OptionGroup.PET_FAMILY.equals(optionGroup)) {
				registerActivity.getPetFamilyText().setText(label);
				registerActivity.setPetFamily(value);
			}
			dialog.cancel();
		}
	};

}
