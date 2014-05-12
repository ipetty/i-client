package net.ipetty.android.sdk.domain;


/**
 * 图片赞
 * 
 * @author xiaojinghai
 */
public class IpetFavor {

	private String id;

	// 赞的话
	private String text;
	// 谁赞的
	private String userId;
	// 谁赞的
	private String userName;
	// 赞什么
	private String photoId;

	private String createAt;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
