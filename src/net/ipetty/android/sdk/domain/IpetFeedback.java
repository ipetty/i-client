package net.ipetty.android.sdk.domain;

/**
 * 意见反馈
 * 
 * @author luocanfeng
 */
public class IpetFeedback {

	// private String type; // 意见类型
	private String title; // 主题
	private String content; // 反馈内容
	private String contact; // 联系方式，非必填，用户未登录的情况下反馈意见时可留下联系方式

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

}
