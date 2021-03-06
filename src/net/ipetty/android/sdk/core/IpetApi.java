package net.ipetty.android.sdk.core;

import android.content.Context;
import net.ipetty.android.sdk.impl.ActivityApiImpl;
import net.ipetty.android.sdk.impl.AppUpdateApiImpl;
import net.ipetty.android.sdk.impl.CrashLogApiImpl;
import net.ipetty.android.sdk.impl.FeedApiImpl;
import net.ipetty.android.sdk.impl.FeedbackApiImpl;
import net.ipetty.android.sdk.impl.FoundationApiImpl;
import net.ipetty.android.sdk.impl.NotificationApiImpl;
import net.ipetty.android.sdk.impl.PetApiImpl;
import net.ipetty.android.sdk.impl.UserApiImpl;
import net.ipetty.sdk.ActivityApi;
import net.ipetty.sdk.AppUpdateApi;
import net.ipetty.sdk.CrashLogApi;
import net.ipetty.sdk.FeedApi;
import net.ipetty.sdk.FeedbackApi;
import net.ipetty.sdk.FoundationApi;
import net.ipetty.sdk.NotificationApi;
import net.ipetty.sdk.PetApi;
import net.ipetty.sdk.UserApi;

/**
 * API门户，负载组装API组件，对用户提供统一访问接口
 *
 * @author xiaojinghai
 */
public class IpetApi extends ApiBase {

	private static IpetApi instance;

	private final UserApi userApi;
	private final PetApi petApi;
	private final FeedApi feedApi;
	private final ActivityApi activityApi;
	private final FeedbackApi feedbackApi;
	private final FoundationApi foundationApi;
	private final AppUpdateApi appUpdateApi;
	private final NotificationApi notificationApi;
	private final CrashLogApi crashLogApiImpl;

	private IpetApi(Context context) {
		super(context);
		userApi = new UserApiImpl(context);
		petApi = new PetApiImpl(context);
		feedApi = new FeedApiImpl(context);
		activityApi = new ActivityApiImpl(context);
		feedbackApi = new FeedbackApiImpl(context);
		foundationApi = new FoundationApiImpl(context);
		appUpdateApi = new AppUpdateApiImpl(context);
		notificationApi = new NotificationApiImpl(context);
		crashLogApiImpl = new CrashLogApiImpl(context);
	}

	public static IpetApi init(Context context) {
		if (null == instance) {
			instance = new IpetApi(context);
		}
		return instance;
	}

	public UserApi getUserApi() {
		return userApi;
	}

	public PetApi getPetApi() {
		return petApi;
	}

	public FeedApi getFeedApi() {
		return feedApi;
	}

	public ActivityApi getActivityApi() {
		return activityApi;
	}

	public FeedbackApi getFeedbackApi() {
		return feedbackApi;
	}

	public FoundationApi getFoundationApi() {
		return foundationApi;
	}

	public AppUpdateApi getAppUpdateApi() {
		return appUpdateApi;
	}

	public NotificationApi getNotificationApi() {
		return notificationApi;
	}

	public CrashLogApi getCrashLogApi() {
		return crashLogApiImpl;
	}

}
