/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.task.feed;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedVO;

/**
 *
 * @author Administrator
 */
public class PublishComment extends Task<CommentVO, FeedVO> {

    public PublishComment(Activity activity) {
        super(activity);
    }

    @Override
    protected FeedVO myDoInBackground(CommentVO... args) {
        return IpetApi.init(activity).getFeedApi().comment(args[0]);
    }

}
