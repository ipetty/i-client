package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.feedback.FeedbackActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.Logout;
import net.ipetty.android.user.UserActivity;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

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

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_setting));
		btnBack.setOnClickListener(new BackClickListener(this));

		/* logout */
		logout = (Button) this.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
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

		/* feedback */
		View feedback = this.findViewById(R.id.feedback_layout);
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SettingActivity.this, FeedbackActivity.class);
				startActivity(intent);
			}
		});
		// load;
		int id = IpetApi.init(this).getCurrUserId();
		UserVO user = UserApiWithCache.getUserById4Synchronous(this, id);

		ImageView avatar = (ImageView) this.findViewById(R.id.avatar);
		if (StringUtils.isNotBlank(user.getAvatar())) {
			String str = Constant.FILE_SERVER_BASE + user.getAvatar();
			ImageLoader.getInstance().displayImage(str, avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}
		TextView name = (TextView) this.findViewById(R.id.name);
		name.setText(user.getNickname());
	}

}
