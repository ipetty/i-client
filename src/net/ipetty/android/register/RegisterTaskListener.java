/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.register;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ActivityManager;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.vo.UserVO;

/**
 *
 * @author yneos
 */
public class RegisterTaskListener extends DefaultTaskListener<UserVO> {

    private final static String TAG = RegisterTaskListener.class.getSimpleName();

    public RegisterTaskListener(Activity activity) {
        super(activity, "正在注册...");
    }

    @Override
    public void onSuccess(UserVO result) {
        Log.d(TAG, "onSuccess");
        this.goMain();
    }

    // 转向主界面
    public void goMain() {
        Log.i(TAG, "to MainActivity");
        AppUtils.goTo(activity, MainActivity.class);
        ActivityManager.getInstance().finish();
    }

}
