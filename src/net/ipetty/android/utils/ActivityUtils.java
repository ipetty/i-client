package net.ipetty.android.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class ActivityUtils {
	public final static String TAG = "ActivityUtils";
	private final List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityUtils instance;

	private ActivityUtils() {
	}

	public static ActivityUtils getInstance() {
		if (null == instance) {
			instance = new ActivityUtils();
		}
		return instance;
	}

	// 退出栈顶Activity
	public void removeActivity(Activity activity) {
		if (activity != null) {
			// 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
			activity.finish();
			activityList.remove(activity);
			activity = null;
		}
	}

	public void distoryActivity(Activity activity) {
		if (activity != null) {
			activityList.remove(activity);
			activity = null;
		}
	}

	// 获得当前栈顶Activity
	public Activity currentActivity() {
		Activity activity = null;
		if (!activityList.isEmpty())
			activity = activityList.get(activityList.size() - 1);
		return activity;
	}

	// 将当前Activity推入栈中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	public void finish() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}