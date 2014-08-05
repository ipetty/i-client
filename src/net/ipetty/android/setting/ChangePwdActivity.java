package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePwdActivity extends BaseActivity {
	private boolean psdDisplayFlg = false;
	private TextView toggleView = null;
	private String password = null;
	private EditText passwordView;
	private EditText oldPasswordView;

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

		passwordView = (EditText) this.findViewById(R.id.password);
		oldPasswordView = (EditText) this.findViewById(R.id.oldPassword);

		View btnView = (View) this.findViewById(R.id.button);
		btnView.setOnClickListener(btnOnClick);
	}

	// 密码可见
	private OnClickListener togglePasswordClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int index = passwordView.getSelectionStart();
			if (!psdDisplayFlg) {
				// display password text, for example "123456"
				// passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_hide);
			} else {
				// hide password, display "."
				// passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
				passwordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				toggleView.setText(R.string.login_toggle_password_show);
			}
			psdDisplayFlg = !psdDisplayFlg;
			Editable etable = passwordView.getText();
			Selection.setSelection(etable, index);
		}
	};

	private OnClickListener btnOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};
}
