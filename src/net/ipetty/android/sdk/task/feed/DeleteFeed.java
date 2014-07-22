/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.feed;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;

/**
 *
 * @author Administrator
 */
public class DeleteFeed extends Task<Long, Boolean> {

    public DeleteFeed(Activity activity) {
        super(activity);
    }

    @Override
    protected Boolean myDoInBackground(Long... args) {
        return IpetApi.init(activity).getFeedApi().delete(args[0]);
    }

}
