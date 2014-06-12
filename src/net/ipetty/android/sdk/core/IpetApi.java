package net.ipetty.android.sdk.core;

import android.content.Context;
import net.ipetty.android.sdk.impl.ActivityApiImpl;
import net.ipetty.android.sdk.impl.FeedApiImpl;
import net.ipetty.android.sdk.impl.FeedbackApiImpl;
import net.ipetty.android.sdk.impl.UserApiImpl;
import net.ipetty.sdk.ActivityApi;
import net.ipetty.sdk.FeedApi;
import net.ipetty.sdk.FeedbackApi;
import net.ipetty.sdk.UserApi;

/**
 * API门户，负载组装API组件，对用户提供统一访问接口
 *
 * @author xiaojinghai
 */
public class IpetApi extends ApiBase {

    private static IpetApi instance;

    private final UserApi userApi;
    private final FeedApi feedApi;
    private final ActivityApi activityApi;
    private final FeedbackApi feedbackApi;

    private IpetApi(Context context) {
        super(context);
        userApi = new UserApiImpl(context);
        feedApi = new FeedApiImpl(context);
        activityApi = new ActivityApiImpl(context);
        feedbackApi = new FeedbackApiImpl(context);

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

    public FeedApi getFeedApi() {
        return feedApi;
    }

    public ActivityApi getActivityApi() {
        return activityApi;
    }

    public FeedbackApi getFeedbackApi() {
        return feedbackApi;
    }

}
