package net.ipetty.android.register;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.ValidUtils;
import net.ipetty.android.sdk.task.foundation.ListOptions;
import net.ipetty.android.sdk.task.user.UserRegister;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.RegisterVO;
import org.apache.commons.lang3.StringUtils;

public class RegisterActivity extends BaseActivity {

	private String TAG = RegisterActivity.class.getSimpleName();

	private AutoCompleteTextView emailEditor;
	private EditText passwordEditor;
	private TextView passwordToggleView = null;
	private EditText nicknameEditor;
	private EditText petNameEditor;
	private Dialog petGenderDialog;
	private List<ModDialogItem> petGenderItems = new ArrayList<ModDialogItem>();
	private TextView petGenderText;
	private String petGender;
	private Dialog petFamilyDialog;
	private List<ModDialogItem> petFamilyItems = new ArrayList<ModDialogItem>();
	private TextView petFamilyText;
	private String petFamily;
	private Button submitButton;

	private boolean displayPasswordFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register));
		btnBack.setOnClickListener(new BackClickListener(this));

		emailEditor = (AutoCompleteTextView) this.findViewById(R.id.account);

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(this).getAccounts();
		final List<String> emailList = new ArrayList<String>();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				emailList.add(account.name);
			}
		}
		String emails[] = new String[emailList.size()];
		if (emailList.size() > 0) {
			for (int i = 0, len = emailList.size(); i < len; i++) {
				emails[i] = emailList.get(i);
			}
		}

		//自动提示
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				emails);
		emailEditor.setAdapter(adapt);

		emailEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {//如果组件失去焦点
					String emailstr = emailEditor.getText().toString();
					String nickName = nicknameEditor.getText().toString();
					if (StringUtils.isBlank(nickName) && emailstr.contains("@")) {
						nicknameEditor.setText(emailstr.split("@")[0]);
					}
				} else {
					if (emailList.size() > 0) {
						emailEditor.showDropDown();
					}
				}
			}
		});

		passwordEditor = (EditText) this.findViewById(R.id.password);

		passwordToggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		passwordToggleView.setOnClickListener(passwordToggleClick);

		nicknameEditor = (EditText) this.findViewById(R.id.nickname);

		nicknameEditor.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				petNameEditor.setText(s + "的爱宠");
			}

			public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {

			}

			public void afterTextChanged(Editable edtbl) {

			}
		});

		petNameEditor = (EditText) this.findViewById(R.id.pet_name);

		petGenderText = (TextView) this.findViewById(R.id.pet_gender);
		petFamilyText = (TextView) this.findViewById(R.id.pet_family);

		new ListOptions(RegisterActivity.this)
				.setListener(
						new ListOptionsTaskListener(RegisterActivity.this, OptionGroup.PET_GENDER)).execute(
						OptionGroup.PET_GENDER);
		new ListOptions(RegisterActivity.this)
				.setListener(
						new ListOptionsTaskListener(RegisterActivity.this, OptionGroup.PET_FAMILY)).execute(
						OptionGroup.PET_FAMILY);

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
			register.setPetGender(petGender);

			// pet family
			register.setPetFamily(petFamily);

			new UserRegister(RegisterActivity.this).setListener(
					new RegisterTaskListener(RegisterActivity.this, register)).execute(register);
		}
	};

	public List<ModDialogItem> getPetGenderItems() {
		return petGenderItems;
	}

	public List<ModDialogItem> getPetFamilyItems() {
		return petFamilyItems;
	}

	public Dialog getPetGenderDialog() {
		return petGenderDialog;
	}

	public Dialog getPetFamilyDialog() {
		return petFamilyDialog;
	}

	public TextView getPetGenderText() {
		return petGenderText;
	}

	public TextView getPetFamilyText() {
		return petFamilyText;
	}

	public void setPetGender(String petGender) {
		this.petGender = petGender;
	}

	public void setPetFamily(String petFamily) {
		this.petFamily = petFamily;
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.register, menu); return true; }
	 */
}
