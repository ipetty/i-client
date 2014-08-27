package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.core.SDKStateManager;
import net.ipetty.android.sdk.task.user.ChangeUserPassword;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePwdActivity extends BaseActivity {

	private boolean psdDisplayFlg = false;
	private TextView toggleView = null;
	private EditText oldPasswordEditor;
	private EditText passwordEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_change_password));
		btnBack.setOnClickListener(new BackClickListener(this));

		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);

		oldPasswordEditor = (EditText) this.findViewById(R.id.oldPassword);
		passwordEditor = (EditText) this.findViewById(R.id.password);

		View btnView = (View) this.findViewById(R.id.button);
		btnView.setOnClickListener(btnOnClick);
	}

	// 密码可见
	private OnClickListener togglePasswordClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int index = passwordEditor.getSelectionStart();
			if (!psdDisplayFlg) {
				// display password text, for example "123456"
				// passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_hide);
			} else {
				// hide password, display "."
				// passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				passwordEditor.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_show);
			}
			psdDisplayFlg = !psdDisplayFlg;
			Editable etable = passwordEditor.getText();
			Selection.setSelection(etable, index);
		}
	};

	private OnClickListener btnOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String platformName = SDKStateManager.getPlatformName(ChangePwdActivity.this);
			String oldPassword = ChangePwdActivity.this.oldPasswordEditor.getText().toString();
			String password = ChangePwdActivity.this.passwordEditor.getText().toString();

			if (StringUtils.isEmpty(platformName) && StringUtils.isEmpty(oldPassword)) {
				ChangePwdActivity.this.oldPasswordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, R.string.old_password_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			if (StringUtils.isEmpty(password)) {
				ChangePwdActivity.this.passwordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, R.string.new_password_empty, Toast.LENGTH_SHORT).show();
				return;
			} else if (password.length() < 6) {
				ChangePwdActivity.this.passwordEditor.requestFocus();
				Toast.makeText(ChangePwdActivity.this, "密码不得少于6位", Toast.LENGTH_SHORT).show();
				return;
			}

			new ChangeUserPassword(ChangePwdActivity.this).setListener(
					new DefaultTaskListener<Boolean>(ChangePwdActivity.this) {
						@Override
						public void onSuccess(Boolean result) {
							Toast.makeText(ChangePwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
							ChangePwdActivity.this.finish();
						}
					}).execute(oldPassword, password);
		}
	};

}
