package net.ipetty.android.core.ui;

import net.ipetty.android.core.Constant;
import net.ipetty.android.space.SpaceActivity;
import android.app.Activity;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class UserURLSpan extends ClickableSpan {
	private Activity activity;
	private int userId;

	public UserURLSpan(Activity activity) {
		this.activity = activity;
	}

	public UserURLSpan(Activity activity, int userId) {
		this.activity = activity;
		this.userId = userId;
		// TODO： 未完待续
	}

	@Override
	public void onClick(View widget) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(activity, SpaceActivity.class);
		intent.putExtra(Constant.INTENT_USER_ID_KEY, userId);
		activity.startActivity(intent);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		ds.setUnderlineText(false);
	}
}