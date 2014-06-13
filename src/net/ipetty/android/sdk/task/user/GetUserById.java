/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.user;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;

/**
 *
 * @author Administrator
 */
public class GetUserById extends Task<Integer, UserVO> {

    public GetUserById(Activity activity) {
        super(activity);
    }

    @Override
    protected UserVO myDoInBackground(Integer... args) {
        return IpetApi.init(activity).getUserApi().getById(args[0]);
    }

}
