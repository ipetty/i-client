package net.ipetty.android.user;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.vo.Option;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ListOptionsTaskListener extends DefaultTaskListener<List<Option>> {

	private UserActivity userActivity;
	private Dialog genderDialog;
	private List<ModDialogItem> dialogItems;
	private EditText genderEditor;

	public ListOptionsTaskListener(Activity activity) {
		super(activity);
	}

	@Override
	public void onSuccess(List<Option> result) {
		userActivity = (UserActivity) super.activity;
		genderEditor = userActivity.getGenderEditor();
		genderDialog = userActivity.getGenderDialog();

		dialogItems = new ArrayList<ModDialogItem>();
		for (Option option : result) {
			dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
		}

		genderEditor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				genderDialog = DialogUtils.modPopupDialog(userActivity, dialogItems, genderDialog);
			}
		});
	}

	private OnClickListener dialogClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
			userActivity.getGenderEditor().setText(label);
			userActivity.setGender(value);
			genderDialog.cancel();
		}
	};

}
