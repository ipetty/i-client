package net.ipetty.android.feedback;

import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.vo.FeedbackVO;
import android.app.Activity;
import android.widget.Toast;

/**
 * FeedbackTaskListener
 * 
 * @author luocanfeng
 * @date 2014年6月17日
 */
public class FeedbackTaskListener extends DefaultTaskListener<FeedbackVO> {

	public FeedbackTaskListener(Activity activity) {
		super(activity, activity.getString(R.string.submitting));
	}

	@Override
	public void onSuccess(FeedbackVO result) {
		Toast.makeText(activity, R.string.feedback_success, Toast.LENGTH_SHORT).show();
		activity.finish();
	}

}
