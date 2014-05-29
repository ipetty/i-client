package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.comment.CommentActivity;
import net.ipetty.android.core.ui.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeRegisterOrLoginActivity extends BaseActivity {
	public final static String TAG = "WelcomeRegisterOrLoginActivity";

	private Button enterBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_register_or_login);
		Log.i(TAG, "onCreate");

		enterBtn = (Button) this.findViewById(R.id.enter_btn);
		enterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeRegisterOrLoginActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		Button btn = (Button) this.findViewById(R.id.enter_btn_test);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeRegisterOrLoginActivity.this, CommentActivity.class);
				startActivity(intent);
			}
		});

		Button btn1 = (Button) this.findViewById(R.id.enter_btn_test1);
		btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeRegisterOrLoginActivity.this, LoginHasAccountActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_register_or_login, menu);
		return true;
	}

}
