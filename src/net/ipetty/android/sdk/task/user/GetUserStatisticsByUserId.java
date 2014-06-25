/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.user;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserStatisticsVO;

/**
 *
 * @author Administrator
 */
public class GetUserStatisticsByUserId extends Task<Integer, UserStatisticsVO> {

    public GetUserStatisticsByUserId(Activity activity) {
        super(activity);
    }

    @Override
    protected UserStatisticsVO myDoInBackground(Integer... args) {
        return IpetApi.init(activity).getUserApi().getUserStatisticsByUserId(args[0]);
    }

}
