/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.login;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * 
 * @author yneos
 */
public class LoginTaskListener extends DefaultTaskListener<UserVO> {

	public LoginTaskListener(Activity activity) {
		super(activity, "正在登录...");
	}

	@Override
	public void onSuccess(UserVO result) {
		Log.d(TAG, "onSuccess");
		AppUtils.goTo(activity, MainActivity.class);
		activity.finish();
	}

}
