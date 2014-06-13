/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.user;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;

/**
 *
 * @author Administrator
 */
public class Unfollow extends Task<Integer, Boolean> {

    public Unfollow(Activity activity) {
        super(activity);
    }

    @Override
    protected Boolean myDoInBackground(Integer... args) {
        return IpetApi.init(activity).getUserApi().unfollow(args[0]);
    }

}
