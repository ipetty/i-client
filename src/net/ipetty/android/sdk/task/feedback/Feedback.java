/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.feedback;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.FeedbackVO;

/**
 *
 * @author Administrator
 */
public class Feedback extends Task<String, FeedbackVO> {

    public Feedback(Activity activity) {
        super(activity);
    }

    @Override
    protected FeedbackVO myDoInBackground(String... args) {
        return IpetApi.init(activity).getFeedbackApi().feedback(args[0], args[1], args[2]);
    }

}
