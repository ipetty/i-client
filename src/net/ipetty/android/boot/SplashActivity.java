package net.ipetty.android.boot;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.register.Register3rdActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.update.UpdateManager;
import net.ipetty.android.update.UpdateUtils;
import net.ipetty.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

/**
 * 启动引导
 *
 * @author Administrator
 *
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_splash);
		Log.d(TAG, "onCreate");
		init();
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		new CheckUpdateTask(this)
				.setListener(new DefaultTaskListener<Boolean>(this) {
					@Override
					public void onSuccess(Boolean hasUpdate) {

						//如果有更新则停留此界面进行升级
						if (hasUpdate) {
							UpdateManager updateManager = new UpdateManager(SplashActivity.this);
							updateManager.setOnCancelListener(new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									SplashActivity.this.startUP();
								}
							});
							updateManager.showNoticeDialog(UpdateUtils.getUpdaeInfo());
						} else {
							//启动流程
							SplashActivity.this.startUP();
						}

					}
				})
				.execute();
	}

	private void init() {
		TextView version = (TextView) this.findViewById(R.id.version_info);
		String verStr = getResources().getString(R.string.app_version);
		String VersionName = String.format(verStr, AppUtils.getAppVersionName(this));
		version.setText(VersionName);
	}

	public void startUP() {
		IpetApi api = IpetApi.init(this);
		// 是否就已认证状态
		if (api.getIsAuthorized()) {
			UserVO currentUser = api.getCurrUserInfo();
			if (currentUser != null && StringUtils.isNotEmpty(currentUser.getEmail())) { // 跳转到首页
				goMain();
			} else { // 跳转到完善资料页面
				AppUtils.goTo(this, Register3rdActivity.class);
				finish();
			}
		} else {
			// 以前没有登录过
			if (api.getCurrUserId() == Constant.EMPTY_USER_ID) {
				// 欢迎界面
				goWelcomeLogin();
			} else {
				// 有过登录帐号
				goHasAccountLogin();
			}
		}
	}

	// 转向主界面
	public void goMain() {
		Log.d(TAG, "to MainActivity");
		AppUtils.goTo(this, MainActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}

	// 转向登陆界面
	public void goWelcomeLogin() {
		Log.d(TAG, "to WelcomeRegisterOrLoginActivity");
		AppUtils.goTo(this, WelcomeRegisterOrLoginActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}

	// 转向已有账号登陆
	public void goHasAccountLogin() {
		Log.d(TAG, "to LoginHasAccountActivity");
		AppUtils.goTo(this, LoginHasAccountActivity.class);
		AnimUtils.fadeInToOut(this);
		finish();
	}

	//转向资料完善页面
	public void goRegister3rdActivity() {
		AppUtils.goTo(this, Register3rdActivity.class);
		finish();
	}

}
