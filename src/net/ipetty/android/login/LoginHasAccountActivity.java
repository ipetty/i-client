package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.UserLogin;
import net.ipetty.sharesdk.sinaweibo.SinaWeiboAuthorization;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginHasAccountActivity extends BaseActivity {

	DisplayImageOptions options;

	private EditText passwordView;
	private TextView toggleView = null;
	private boolean psdDisplayFlg = false;
	private TextView account = null;
	private ImageView avatar;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_has_account);

		progressDialog = new ProgressDialog(LoginHasAccountActivity.this);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getResources().getString(R.string.logining));
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_login_has_account));
		btnBack.setOnClickListener(new BackClickListener(this));

		TextView changeAccount = (TextView) this.findViewById(R.id.changeAccount);
		changeAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginHasAccountActivity.this, LoginActivity.class);
				LoginHasAccountActivity.this.startActivity(intent);
			}
		});

		options = AppUtils.getRoundedImageOptions();
		account = (TextView) this.findViewById(R.id.account);

		// avator
		avatar = (ImageView) this.findViewById(R.id.avatar);

		// passworid
		passwordView = (EditText) this.findViewById(R.id.password);
		toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
		toggleView.setOnClickListener(togglePasswordClick);
		// login btn
		Button loginBtn = (Button) this.findViewById(R.id.button);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new UserLogin(LoginHasAccountActivity.this).setListener(
						new LoginTaskListener(LoginHasAccountActivity.this)).execute(account.getText().toString(),
						passwordView.getText().toString());
			}
		});

		// sina Login
		View sina = this.findViewById(R.id.sina);
		sina.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareSDK.initSDK(LoginHasAccountActivity.this);
				Platform sinaWeibo = ShareSDK.getPlatform(LoginHasAccountActivity.this, SinaWeibo.NAME);
				new SinaWeiboAuthorization(LoginHasAccountActivity.this).authorize(sinaWeibo);
				progressDialog.show();
			}
		});

		// qq login
		View qq = this.findViewById(R.id.qq);
		qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginHasAccountActivity.this, "正在和企鹅沟通中，即将启用~", Toast.LENGTH_SHORT).show();
				// ShareSDK.initSDK(LoginHasAccountActivity.this);
				// Platform qzone =
				// ShareSDK.getPlatform(LoginHasAccountActivity.this,
				// QZone.NAME);
				// new
				// QZoneAuthorization(LoginHasAccountActivity.this).authorize(qzone);
				// progressDialog.show();
			}
		});

		loadData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog.dismiss();
	}

	private void loadData() {
		IpetApi api = IpetApi.init(this);
		Integer currUserId = api.getCurrUserId();
		UserVO user = UserApiWithCache.getUserById4Synchronous(this, currUserId);
		if (!StringUtils.isEmpty(user.getEmail())) {
			account.setText(user.getEmail());
		}

		if (StringUtils.isNotBlank(user.getAvatar())) {
			String str = Constant.FILE_SERVER_BASE + user.getAvatar();
			ImageLoader.getInstance().displayImage(str, avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}
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
