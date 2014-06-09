package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginHasAccountActivity extends BaseActivity {
	DisplayImageOptions options;

	private EditText passwordView;
	private TextView toggleView = null;
	private boolean psdDisplayFlg = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_has_account);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_login_has_account));
		btnBack.setOnClickListener(new BackClickListener(this));

		TextView changeAccount = (TextView) this.findViewById(R.id.changeAccount);
		changeAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginHasAccountActivity.this, LoginActivity.class);
				LoginHasAccountActivity.this.startActivity(intent);
				// LoginHasAccountActivity.this.finish();
			}
		});

		String textUrl = "http://weibo.kedacom.com/weibo/files/h/b9c31599803e48f0a0595e2e913714e4/h64.jpg?t=1388814731997";

		options = AppUtils.getRoundedImageOptions();

		// avator
		ImageView avator = (ImageView) this.findViewById(R.id.avator);
		ImageLoader.getInstance().displayImage(textUrl, avator, options);
		// passworid
		passwordView = (EditText) this.findViewById(R.id.password);
		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);

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

}
