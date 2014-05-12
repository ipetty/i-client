package net.ipetty.android.sdk;

/**
 * 意见反馈API
 * 
 * @author luocanfeng
 */
public interface FeedbackApi {

	// public boolean feedback(IpetFeedback feedback);

	public boolean feedback(String title, String content, String contact);

}
