/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.register;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.login.LoginTaskListener;
import net.ipetty.android.sdk.task.user.UserLogin;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.util.Log;

/**
 * 
 * @author yneos
 */
public class RegisterTaskListener extends DefaultTaskListener<UserVO> {

	private final static String TAG = RegisterTaskListener.class.getSimpleName();

	private RegisterVO register;

	public RegisterTaskListener(Activity activity, RegisterVO register) {
		super(activity, "正在注册...");
		this.register = register;
	}

	@Override
	public void onSuccess(UserVO result) {
		Log.d(TAG, "onSuccess");
		new UserLogin(activity).setListener(new LoginTaskListener(activity)).execute(register.getEmail(),
				register.getPassword());
	}

}
