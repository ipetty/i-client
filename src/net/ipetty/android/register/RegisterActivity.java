package net.ipetty.android.register;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DialogUtils;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {

	private Dialog petGenderDialog;
	private Dialog petFamilyDialog;
	private EditText emailText;
	private EditText passwordText;
	private TextView petGenderText;
	private TextView petFamilyText;
	private TextView passwordToggleView = null;
	private boolean displayPasswordFlag = false;
	private List<ModDialogItem> petGenderItems;
	private List<ModDialogItem> petFamilyItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register));
		btnBack.setOnClickListener(new BackClickListener(this));

		emailText = (EditText) this.findViewById(R.id.account);
		passwordText = (EditText) this.findViewById(R.id.password);

		passwordToggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		passwordToggleView.setOnClickListener(passwordToggleClick);

		petGenderItems = new ArrayList<ModDialogItem>();
		petGenderItems.add(new ModDialogItem(null, "男生", petGenderClick));
		petGenderItems.add(new ModDialogItem(null, "女生", petGenderClick));
		petGenderItems.add(new ModDialogItem(null, "男女生", petGenderClick));

		petGenderText = (TextView) this.findViewById(R.id.sex);
		petGenderText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				petGenderDialog = DialogUtils.modPopupDialog(RegisterActivity.this, petGenderItems, petGenderDialog);
			}
		});

		petFamilyItems = new ArrayList<ModDialogItem>();
		petFamilyItems.add(new ModDialogItem(null, "汪星人", petFamilyClick));
		petFamilyItems.add(new ModDialogItem(null, "喵星人", petFamilyClick));
		petFamilyItems.add(new ModDialogItem(null, "水星人", petFamilyClick));
		petFamilyItems.add(new ModDialogItem(null, "冷星人", petFamilyClick));
		petFamilyItems.add(new ModDialogItem(null, "异星人", petFamilyClick));

		petFamilyText = (TextView) this.findViewById(R.id.type);
		petFamilyText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				petFamilyDialog = DialogUtils.modPopupDialog(RegisterActivity.this, petFamilyItems, petFamilyDialog);
			}
		});
	}

	// 密码可见
	private OnClickListener passwordToggleClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			int index = passwordText.getSelectionStart();
			if (!displayPasswordFlag) {
				// display password text, for example "123456"
				// passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				passwordToggleView.setText(R.string.login_toggle_password_hide);
			} else {
				// hide password, display "."
				// passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				passwordToggleView.setText(R.string.login_toggle_password_show);
			}
			displayPasswordFlag = !displayPasswordFlag;
			Editable etable = passwordText.getText();
			Selection.setSelection(etable, index);
		}
	};

	private OnClickListener petGenderClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String text = ((TextView) view.findViewById(R.id.text)).getText().toString();
			String value = ((TextView) view.findViewById(R.id.value)).getText().toString(); // 这里是value值
			petGenderText.setText(text);
			petGenderDialog.cancel();
		}
	};

	private OnClickListener petFamilyClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
			petFamilyText.setText(text);
			petFamilyDialog.cancel();
		}
	};
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.register, menu); return true; }
	 */

}
