package net.ipetty.android.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class AnimUtils {

	public static void fadeInToOut(Activity activity) {
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public static DisplayMetrics getDefaultDisplayMetrics(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
