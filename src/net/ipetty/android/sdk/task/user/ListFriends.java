/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.user;

import java.util.List;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;
import android.app.Activity;

/**
 * 
 * @author Administrator
 */
public class ListFriends extends Task<Integer, List<UserVO>> {

	public ListFriends(Activity activity) {
		super(activity);
	}

	@Override
	protected List<UserVO> myDoInBackground(Integer... args) {
		return IpetApi.init(activity).getUserApi().listFriends(args[0], args[1], args[2]);
	}

}
