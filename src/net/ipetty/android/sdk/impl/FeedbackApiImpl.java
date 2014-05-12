package net.ipetty.android.sdk.impl;

import java.net.URI;

import net.ipetty.android.sdk.FeedbackApi;
import net.ipetty.android.sdk.base.ApiBase;
import net.ipetty.android.sdk.base.ApiContext;
import net.ipetty.android.sdk.domain.IpetFeedback;

import org.springframework.util.LinkedMultiValueMap;

public class FeedbackApiImpl extends ApiBase implements FeedbackApi {

	public FeedbackApiImpl(ApiContext context) {
		super(context);
	}

	private boolean feedback(IpetFeedback feedback) {
		URI uri = buildUri("feedback/feedback");
		LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
		body.add("title", feedback.getTitle());
		body.add("content", feedback.getContent());
		body.add("contact", feedback.getContact());
		body.add("userId", context.getCurrUserId());
		Boolean result = context.getRestTemplate().postForObject(uri, body, Boolean.class);
		return result;
	}

	@Override
	public boolean feedback(String title, String content, String contact) {
		IpetFeedback feedback = new IpetFeedback();
		feedback.setTitle(title);
		feedback.setContent(content);
		feedback.setContact(contact);
		return feedback(feedback);
	}

}
