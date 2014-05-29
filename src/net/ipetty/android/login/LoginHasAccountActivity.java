package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoginHasAccountActivity extends BaseActivity {
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_has_account);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_login_has_account));
		btnBack.setOnClickListener(new BackClickListener(this));

		String textUrl = "http://weibo.kedacom.com/weibo/files/h/b9c31599803e48f0a0595e2e913714e4/h64.jpg?t=1388814731997";

		options = AppUtils.getRoundedImageOptions();

		// avator
		ImageView avator = (ImageView) this.findViewById(R.id.avator);
		ImageLoader.getInstance().displayImage(textUrl, avator, options);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_has_account, menu);
		return true;
	}

}
