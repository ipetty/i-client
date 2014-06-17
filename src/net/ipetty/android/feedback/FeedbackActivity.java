package net.ipetty.android.feedback;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.task.feedback.Feedback;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends BaseActivity {

	private View submitButton;
	private EditText contentEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		/* action bar */
		ImageView goBackButton = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView title = (TextView) this.findViewById(R.id.action_bar_title);
		title.setText(this.getResources().getString(R.string.title_activity_feedback));
		goBackButton.setOnClickListener(new BackClickListener(this));

		submitButton = this.findViewById(R.id.feedback_btn);
		contentEditor = (EditText) this.findViewById(R.id.editText);
		submitButton.setOnClickListener(sumbit);
	}

	private final OnClickListener sumbit = new OnClickListener() {
		@Override
		public void onClick(View view) {
			String feedbackContent = FeedbackActivity.this.contentEditor.getText().toString();
			if (StringUtils.isEmpty(feedbackContent)) {
				FeedbackActivity.this.contentEditor.requestFocus();
				Toast.makeText(FeedbackActivity.this, R.string.feedback_no_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			new Feedback(FeedbackActivity.this).setListener(new FeedbackTaskListener(FeedbackActivity.this)).execute(
					null, feedbackContent, null);
		}
	};

}
