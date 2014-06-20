package net.ipetty.android.setting;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.feedback.FeedbackActivity;
import net.ipetty.android.sdk.task.user.Logout;
import net.ipetty.android.user.UserActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends Activity {

	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

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
		View user = this.findViewById(R.id.user_layout);
		user.setOnClickListener(new OnClickListener() {
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
	}

}
