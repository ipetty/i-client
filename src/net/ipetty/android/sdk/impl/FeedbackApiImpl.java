package net.ipetty.android.sdk.impl;

import android.content.Context;
import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.FeedbackApi;
import net.ipetty.vo.FeedbackVO;
import org.springframework.util.LinkedMultiValueMap;

/**
 * FeedbackApiImpl
 *
 * @author luocanfeng
 * @date 2014年6月5日
 */
public class FeedbackApiImpl extends ApiBase implements FeedbackApi {

    public FeedbackApiImpl(Context context) {
        super(context);
    }

    private static final String URI_FEEDBACK = "/feedback";

    /**
     * 反馈意见
     */
    @Override
    public FeedbackVO feedback(String title, String content, String contact) {
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("title", title);
        request.add("content", content);
        request.add("contact", contact);
        return getRestTemplate().postForObject(buildUri(URI_FEEDBACK), request, FeedbackVO.class);
    }

}
