/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.login;

import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.register.Register3rdActivity;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;

/**
 * Login3rdTaskListener
 * 
 * @author luocanfeng
 * @date 2014年7月22日
 */
public class Login3rdTaskListener extends DefaultTaskListener<UserVO> {

	private Platform platform;

	public Login3rdTaskListener(Activity activity, Platform platform) {
		super(activity);
		this.platform = platform;
	}

	@Override
	public void onSuccess(UserVO user) {
		Log.d(TAG, "onSuccess");
		if (user != null && StringUtils.isNotEmpty(user.getEmail())) { // 跳转到首页
			AppUtils.goTo(activity, MainActivity.class);
		} else { // 跳转到完善资料页面
			Toast.makeText(activity, "请完善个人资料", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(activity, Register3rdActivity.class);
			intent.putExtra(Constant.INTENT_PLATFORM_NAME_KEY, platform.getName());
			activity.startActivity(intent);
		}
	}

	@Override
	public void onError(Throwable ex) {
		super.onError(ex);
		platform.removeAccount();
	}

}
