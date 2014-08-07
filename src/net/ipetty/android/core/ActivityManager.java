package net.ipetty.android.core;

import java.util.LinkedList;
import java.util.List;

import net.ipetty.android.service.MessageService;
import android.app.Activity;
import android.content.Intent;
import cn.sharesdk.framework.ShareSDK;

public class ActivityManager {

	private final List<Activity> activityList = new LinkedList<Activity>();
	private static ActivityManager instance;
	private final Intent messageServiceIntent = new Intent(MessageService.class.getName());

	private ActivityManager() {
	}

	public static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
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
		if (!activityList.isEmpty()) {
			activity = activityList.get(activityList.size() - 1);
		}
		return activity;
	}

	// 将当前Activity推入栈中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		int i = 0;

		for (Activity activity : activityList) {
			if (i == 0) {
				activity.stopService(messageServiceIntent);
				ShareSDK.stopSDK(activity);
			}
			activity.finish();
			i++;
		}
		System.exit(0);
	}

	public void finish() {
		for (Activity activity : activityList) {
			activity.finish();
		}
	}
}
