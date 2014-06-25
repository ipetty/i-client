/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserFormVO;
import net.ipetty.vo.UserVO;
import android.app.Activity;

/**
 * UpdateUser
 * 
 * @author luocanfeng
 * @date 2014年6月25日
 */
public class UpdateUser extends Task<UserFormVO, UserVO> {

	public UpdateUser(Activity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(UserFormVO... args) {
		return IpetApi.init(activity).getUserApi().update(args[0]);
	}

}
