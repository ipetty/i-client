package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.boot.CheckUpdateTask;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.feedback.FeedbackActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.core.SDKStateManager;
import net.ipetty.android.sdk.task.user.Logout;
import net.ipetty.android.update.UpdateManager;
import net.ipetty.android.update.UpdateUtils;
import net.ipetty.android.user.UserActivity;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends BaseActivity {

	private Button logout;
	private DisplayImageOptions options = AppUtils.getCacheImageBublder()
			.showImageForEmptyUri(R.drawable.default_image).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_setting));
		btnBack.setOnClickListener(new BackClickListener(this));

		Integer currUserId = IpetApi.init(this).getCurrUserId();
		UserVO user = UserApiWithCache.getUserById4Synchronous(this, currUserId);

		ImageView avatar = (ImageView) this.findViewById(R.id.avatar);
		if (StringUtils.isNotBlank(user.getAvatar())) {
			String str = Constant.FILE_SERVER_BASE + user.getAvatar();
			ImageLoader.getInstance().displayImage(str, avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}
		TextView name = (TextView) this.findViewById(R.id.name);
		name.setText(user.getNickname());

		/* logout */
		logout = (Button) this.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String platformName = SDKStateManager.getPlatformName(SettingActivity.this);
				MyAppStateManager.setLastLogoutPlatform(SettingActivity.this, platformName);
				new Logout(SettingActivity.this).setListener(new LogoutTaskListener(SettingActivity.this)).execute();
			}
		});

		/* userinfo */
		View user_view = this.findViewById(R.id.user_layout);
		user_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this, UserActivity.class);
				startActivity(intent);
			}
		});

		/* password */
		View password = this.findViewById(R.id.password_layout);
		password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this, ChangePwdActivity.class);
				startActivity(intent);
			}
		});
		/* feedback */
		View feedback = this.findViewById(R.id.feedback_layout);
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
				startActivity(intent);
			}
		});

		/* about version */
		TextView about = (TextView) this.findViewById(R.id.about);
		String verStr = getResources().getString(R.string.app_version);
		String VersionName = String.format(verStr, AppUtils.getAppVersionName(this));
		about.setText(about.getText() + VersionName);
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new CheckUpdateTask(SettingActivity.this).setListener(
						new DefaultTaskListener<Boolean>(SettingActivity.this) {
							@Override
							public void onSuccess(Boolean hasUpdate) {
								// 如果有更新
								if (hasUpdate) {
									UpdateManager updateManager = new UpdateManager(SettingActivity.this);
									updateManager.setOnCancelListener(new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									});
									updateManager.showNoticeDialog(UpdateUtils.getUpdaeInfo());
								} else {
									SettingActivity.this.showMessageForShortTime("当前已是最新版本");
								}

							}
						}).execute();
			}
		});

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

	}

}
