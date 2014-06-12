/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.boot;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginHasAccountActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;

/**
 *
 * @author yneos
 */
public class SplashTaskListener extends DefaultTaskListener<Void> {

    private final static String TAG = SplashTaskListener.class.getSimpleName();

    public SplashTaskListener(Activity activity) {
        super(activity);
    }

    @Override
    public void onSuccess(Void result) {
        Log.d(TAG, "onSuccess");
        IpetApi api = IpetApi.init(activity);
        //是否就已认证状态
        if (api.getIsAuthorized()) {
            // 首页
            goMain();
        } else {
            // 以前没有登录过
            if (api.getCurrUserId() == -1) {
                // 欢迎界面
                goWelcomeLogin();
            } else {
                // 有过登录帐号
                goHasAccountLogin();
            }
        }
    }

    // 转向主界面
    public void goMain() {
        Log.i(TAG, "to MainActivity");
        AppUtils.goTo(activity, MainActivity.class);
        AnimUtils.fadeInToOut(this.activity);
        this.activity.finish();
    }

    // 转向登陆界面
    public void goWelcomeLogin() {
        Log.i(TAG, "to WelcomeRegisterOrLoginActivity");
        AppUtils.goTo(activity, WelcomeRegisterOrLoginActivity.class);
        AnimUtils.fadeInToOut(this.activity);
        this.activity.finish();
    }

    // 转向已有账号登陆
    public void goHasAccountLogin() {
        Log.i(TAG, "to LoginHasAccountActivity");
        AppUtils.goTo(activity, LoginHasAccountActivity.class);
        AnimUtils.fadeInToOut(this.activity);
        this.activity.finish();
    }

}
