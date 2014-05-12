package net.ipetty.android.sdk.impl;

import net.ipetty.android.sdk.AccountApi;
import net.ipetty.android.sdk.AppApi;
import net.ipetty.android.sdk.CommentApi;
import net.ipetty.android.sdk.ContactApi;
import net.ipetty.android.sdk.DiscoverApi;
import net.ipetty.android.sdk.FavorApi;
import net.ipetty.android.sdk.FeedbackApi;
import net.ipetty.android.sdk.IpetApi;
import net.ipetty.android.sdk.PhotoApi;
import net.ipetty.android.sdk.UserApi;
import net.ipetty.android.sdk.base.ApiContext;
import android.content.Context;

/**
 * API门户，负载组装API组件，对用户提供统一访问接口
 *
 * @author xiaojinghai
 */
public class IpetApiImpl implements IpetApi {

    private static IpetApi instance;

    private final ApiContext context;

    private final AccountApi accountApi;

    private final UserApi userApi;

    private final ContactApi contactApi;

    private final PhotoApi photoApi;

    private final DiscoverApi discoverApi;

    private final FavorApi favorApi;

    private final CommentApi commentApi;

    private final FeedbackApi feedbackApi;

    private final AppApi appApi;

    private IpetApiImpl(String appKey, String appSecret, Context androidContext) {
        context = ApiContext.getInstace(appKey, appSecret, androidContext);
        accountApi = new AccountApiImpl(context);
        userApi = new UserApiImpl(context);
        contactApi = new ContactApiImpl(context);
        photoApi = new PhotoApiImpl(context);
        discoverApi = new DiscoverApiImpl(context);
        favorApi = new FavorApiImpl(context);
        commentApi = new CommentApiImpl(context);
        feedbackApi = new FeedbackApiImpl(context);
        appApi = new AppApiImpl(context);
    }

    public static synchronized IpetApi getInstance(String appKey, String appSecret, Context androidContext) {
        if (instance == null) {
            instance = new IpetApiImpl(appKey, appSecret, androidContext);
        }
        return instance;
    }

    @Override
    public AccountApi getAccountApi() {
        return accountApi;
    }

    @Override
    public UserApi getUserApi() {
        return userApi;
    }

    @Override
    public ContactApi getContactApi() {
        return contactApi;
    }

    @Override
    public String getCurrUserId() {
        return context.getCurrUserId();
    }

    public PhotoApi getPhotoApi() {
        return photoApi;
    }

    @Override
    public DiscoverApi getDiscoverApi() {
        return discoverApi;
    }

    @Override
    public CommentApi getCommentApi() {
        return commentApi;
    }

    @Override
    public FavorApi getFavorApi() {
        return favorApi;
    }

    @Override
    public FeedbackApi getFeedbackApi() {
        return feedbackApi;
    }

    @Override
    public AppApi getAppApi() {
        return appApi;
    }

}
