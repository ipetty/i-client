/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.activity;

import android.app.Activity;
import java.util.List;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.ActivityVO;

/**
 *
 * @author Administrator
 */
public class ListActivities extends Task<Integer, List<ActivityVO>> {

    public ListActivities(Activity activity) {
        super(activity);
    }

    @Override
    protected List<ActivityVO> myDoInBackground(Integer... args) {
        return IpetApi.init(activity).getActivityApi().listActivities(args[0], args[1]);
    }

}
