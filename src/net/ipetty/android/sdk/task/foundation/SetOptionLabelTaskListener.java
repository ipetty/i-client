package net.ipetty.android.sdk.task.foundation;

import java.util.Map;

import net.ipetty.android.core.DefaultTaskListener;
import android.app.Activity;
import android.widget.TextView;

public class SetOptionLabelTaskListener extends DefaultTaskListener<Map<String, String>> {

	private TextView view;
	private String value;

	public SetOptionLabelTaskListener(Activity activity) {
		super(activity);
	}

	public SetOptionLabelTaskListener(Activity activity, TextView view, String value) {
		super(activity);
		this.view = view;
		this.value = value;
	}

	@Override
	public void onSuccess(Map<String, String> optionValueLabelMap) {
		String label = optionValueLabelMap.get(value);
		view.setText(label == null ? "" : label);
	}

}
