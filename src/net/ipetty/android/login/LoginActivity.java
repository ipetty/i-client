package net.ipetty.android.login;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.register.RegisterActivity;
import net.ipetty.android.sdk.task.user.UserLogin;
import net.ipetty.sharesdk.sinaweibo.SinaWeiboAuthorization;

import org.apache.commons.lang3.StringUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class LoginActivity extends BaseActivity {

	private AutoCompleteTextView accountView;
	private EditText passwordView;
	private String account = null;
	private String password = null;
	private TextView toggleView = null;
	private boolean psdDisplayFlg = false;
	private ProgressDialog progressDialog;

	// private int focuscont = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_login));
		btnBack.setOnClickListener(new BackClickListener(this));

		// 注册
		TextView btnReg = (TextView) this.findViewById(R.id.register);
		btnReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});

		progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getResources().getString(R.string.logining));
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		// 忘记密码
		TextView forgotView = (TextView) this.findViewById(R.id.forget_password);
		forgotView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "暂时未实现", Toast.LENGTH_SHORT).show();
			}
		});

		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);

		accountView = (AutoCompleteTextView) this.findViewById(R.id.account);

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(this).getAccounts();
		final List<String> emailList = new ArrayList<String>(0);
		for (Account acc : accounts) {
			if (emailPattern.matcher(acc.name).matches()) {
				emailList.add(acc.name);
			}
		}
		String emails[] = new String[emailList.size()];
		if (emailList.size() > 0) {
			for (int i = 0, len = emailList.size(); i < len; i++) {
				emails[i] = emailList.get(i);
			}
		}

		// 自动提示
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emails);
		accountView.setAdapter(adapt);

		accountView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (emailList.size() > 0) {
						accountView.showDropDown();
					}
				}
			}
		});

		/*
		 * accountView.setOnFocusChangeListener(new OnFocusChangeListener(){
		 * 
		 * @Override public void onFocusChange(View arg0, boolean hasFocus) {
		 * if(hasFocus){ accountView.setHint(null); if(focuscont==0){
		 * accountView.clearFocus(); } focuscont++; }else{
		 * accountView.setHint("Email"); }
		 * 
		 * }
		 */
		passwordView = (EditText) this.findViewById(R.id.password);
		// 登陆
		View loginBtnView = (View) this.findViewById(R.id.button);
		loginBtnView.setOnClickListener(loginOnClick);

		// sina Login
		View sina = this.findViewById(R.id.sina);
		sina.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareSDK.initSDK(LoginActivity.this);
				Platform sinaWeibo = ShareSDK.getPlatform(LoginActivity.this, SinaWeibo.NAME);
				new SinaWeiboAuthorization(LoginActivity.this).authorize(sinaWeibo);
				progressDialog.show();
			}
		});

		// qq login
		View qq = this.findViewById(R.id.qq);
		qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "正在和企鹅沟通中，即将启用~", Toast.LENGTH_SHORT).show();
				// ShareSDK.initSDK(LoginActivity.this);
				// Platform qzone = ShareSDK.getPlatform(LoginActivity.this,
				// QZone.NAME);
				// new QZoneAuthorization(LoginActivity.this).authorize(qzone);
				// progressDialog.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog.dismiss();
	}

	// 登录
	private final OnClickListener loginOnClick = new OnClickListener() {
		@Override
		public void onClick(View loginBtnView) {
			if (!validateLogin()) {
				return;
			}

			new UserLogin(LoginActivity.this).setListener(new LoginTaskListener(LoginActivity.this)).execute(account,
					password);
		}
	};

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

	// 登录前校验
	private boolean validateLogin() {
		this.account = accountView.getText().toString();
		this.password = passwordView.getText().toString();

		if (StringUtils.isBlank(this.account)) {
			accountView.requestFocus();
			Toast.makeText(LoginActivity.this, R.string.login_empty_account, Toast.LENGTH_SHORT).show();
			return false;
		}

		// if (!ValidUtils.isEmail(this.account)) {
		// accountView.requestFocus();
		// Toast.makeText(LoginActivity.this,
		// R.string.login_error_invalid_email,
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		if (StringUtils.isBlank(this.password)) {
			passwordView.requestFocus();
			Toast.makeText(LoginActivity.this, R.string.login_empty_password, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

}
