package net.ipetty.android.feedback;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends BaseActivity {
	private View feedback_btn;
	private EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_feedback));
		btnBack.setOnClickListener(new BackClickListener(this));

		feedback_btn = this.findViewById(R.id.feedback_btn);
		edit = (EditText) this.findViewById(R.id.editText);
		feedback_btn.setOnClickListener(sumbit);
	}

	private final OnClickListener sumbit = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Editable text = FeedbackActivity.this.edit.getText();
			String str = text.toString();
			if (StringUtils.isEmpty(str)) {
				Toast.makeText(FeedbackActivity.this, R.string.feedback_no_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			// new FeedbackAsyncTask(FeedbackActivity.this).execute(str);
		}

	};

}
