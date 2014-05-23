package net.ipetty.android.ui;

import net.ipetty.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainPopDialog extends BaseActivity {

	private View logout;
	private View feedback;
	private View person;
	private ImageView avator;
	private TextView username;

	public static int POP_INTENT_RESPONSE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pop_dialog);

		// logout = this.findViewById(R.id.menu_logout_layout);
		feedback = this.findViewById(R.id.menu_feedback_layout);
		person = this.findViewById(R.id.menu_person_layout);

		// logout.setOnClickListener(myLogout);
		feedback.setOnClickListener(myFeedback);
		person.setOnClickListener(myPerson);

		// MyApp application = (MyApp) this.getApplication();
		// IpetUser user = application.getUser();

		// avator = (ImageView) this.findViewById(R.id.avator);
		// avator.setImageUrl(user.getAvatar48(), R.drawable.menu_person);

		// username = (TextView) this.findViewById(R.id.username);
		// username.setText(user.getDisplayName());

	}

	private final OnClickListener myPerson = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Toast.makeText(MainPopDialog.this, "尚未实现",
			// Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(MainPopDialog.this,
			// SetUserInfoActivity.class);
			// startActivity(intent);
			// finish();
		}

	};

	private final OnClickListener myFeedback = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainPopDialog.this, FeedbackActivity.class);
			startActivity(intent);
			finish();
		}

	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
}
