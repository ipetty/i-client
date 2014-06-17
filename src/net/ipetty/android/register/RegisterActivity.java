package net.ipetty.android.register;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.ValidUtils;
import net.ipetty.android.sdk.task.user.UserRegister;
import net.ipetty.vo.RegisterVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

// FIXME 从基础数据中加载性别、家族等基础数据
// FIXME 性别、家族等数据应该传递value而非text，但界面上选中后应该显示text
public class RegisterActivity extends BaseActivity {

	private EditText emailEditor;
	private EditText passwordEditor;
	private TextView passwordToggleView = null;
	private EditText nicknameEditor;
	private EditText petNameEditor;
	private Dialog petGenderDialog;
	private List<ModDialogItem> petGenderItems;
	private TextView petGenderText;
	private Dialog petFamilyDialog;
	private List<ModDialogItem> petFamilyItems;
	private TextView petFamilyText;
	private Button submitButton;

	private boolean displayPasswordFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register));
		btnBack.setOnClickListener(new BackClickListener(this));

		emailEditor = (EditText) this.findViewById(R.id.account);
		passwordEditor = (EditText) this.findViewById(R.id.password);

		passwordToggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		passwordToggleView.setOnClickListener(passwordToggleClick);

		nicknameEditor = (EditText) this.findViewById(R.id.nickname);

		petNameEditor = (EditText) this.findViewById(R.id.pet_name);

		petGenderItems = new ArrayList<ModDialogItem>();
		petGenderItems.add(new ModDialogItem(null, "男生", petGenderClick));
		petGenderItems.add(new ModDialogItem(null, "女生", petGenderClick));
		petGenderItems.add(new ModDialogItem(null, "男女生", petGenderClick));

		petGenderText = (TextView) this.findViewById(R.id.pet_gender);
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

		petFamilyText = (TextView) this.findViewById(R.id.pet_family);
		petFamilyText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				petFamilyDialog = DialogUtils.modPopupDialog(RegisterActivity.this, petFamilyItems, petFamilyDialog);
			}
		});

		submitButton = (Button) this.findViewById(R.id.button);
		submitButton.setOnClickListener(sumbit);
	}

	// 密码可见
	private OnClickListener passwordToggleClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			int index = passwordEditor.getSelectionStart();
			if (!displayPasswordFlag) {
				// display password text, for example "123456"
				// passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				passwordToggleView.setText(R.string.login_toggle_password_hide);
			} else {
				// hide password, display "."
				// passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				passwordToggleView.setText(R.string.login_toggle_password_show);
			}
			displayPasswordFlag = !displayPasswordFlag;
			Editable etable = passwordEditor.getText();
			Selection.setSelection(etable, index);
		}
	};

	private OnClickListener petGenderClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String text = ((TextView) view.findViewById(R.id.text)).getText().toString();
			// String value = ((TextView)
			// view.findViewById(R.id.value)).getText().toString(); // 这里是value值
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

	private final OnClickListener sumbit = new OnClickListener() {
		@Override
		public void onClick(View view) {
			RegisterVO register = new RegisterVO();

			// email
			String email = RegisterActivity.this.emailEditor.getText().toString();
			register.setEmail(email);
			if (StringUtils.isEmpty(email)) {
				RegisterActivity.this.emailEditor.requestFocus();
				Toast.makeText(RegisterActivity.this, R.string.login_empty_account, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!ValidUtils.isEmail(email)) {
				RegisterActivity.this.emailEditor.requestFocus();
				Toast.makeText(RegisterActivity.this, R.string.login_error_invalid_email, Toast.LENGTH_SHORT).show();
				return;
			}

			// password
			String password = RegisterActivity.this.passwordEditor.getText().toString();
			register.setPassword(password);
			if (StringUtils.isEmpty(password)) {
				RegisterActivity.this.passwordEditor.requestFocus();
				Toast.makeText(RegisterActivity.this, R.string.login_empty_password, Toast.LENGTH_SHORT).show();
				return;
			}

			// nickname
			String nickname = RegisterActivity.this.nicknameEditor.getText().toString();
			register.setNickname(nickname);

			// TODO gender

			// pet name
			String petName = RegisterActivity.this.petNameEditor.getText().toString();
			register.setPetName(petName);

			// pet gender
			String petGender = RegisterActivity.this.petGenderText.getText().toString();
			register.setPetGender(petGender);

			// pet family
			String petFamily = RegisterActivity.this.petFamilyText.getText().toString();
			register.setPetFamily(petFamily);

			new UserRegister(RegisterActivity.this).execute(register);
		}
	};

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.register, menu); return true; }
	 */

}
