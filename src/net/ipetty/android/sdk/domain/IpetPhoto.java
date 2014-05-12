package net.ipetty.android.sdk.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.ipetty.android.sdk.base.ApiContext;

/**
 * 
 * @author xiaojinghai
 */
public class IpetPhoto implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1668704192767921961L;

	private String id;
	// 原图
	private String originalURL;
	// 缩略图
	private String smallURL;
	// 文字描述
	private String text;
	// 转发自
	private String forwordFromId;
	// 评论数量
	private String commentCount;
	// 赞数量
	private String favorCount = "0";
	// 所属用户
	private String userId;

	private String userName;

	private String avatar48;

	private String createAt;

	// 我是否赞过
	private boolean favored;

	private List<IpetComment> comments = new ArrayList<IpetComment>();

	public String getOriginalURL() {
		return ApiContext.FILE_SERVER_BASE + originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getSmallURL() {
		return ApiContext.FILE_SERVER_BASE + smallURL;
	}

	public void setSmallURL(String smallURL) {
		this.smallURL = smallURL;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getForwordFromId() {
		return forwordFromId;
	}

	public void setForwordFromId(String forwordFromId) {
		this.forwordFromId = forwordFromId;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(String favorCount) {
		this.favorCount = favorCount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar48() {
		return ApiContext.FILE_SERVER_BASE + avatar48;
	}

	public void setAvatar48(String avatar48) {
		this.avatar48 = avatar48;
	}

	public boolean isFavored() {
		return favored;
	}

	public void setFavored(boolean favored) {
		this.favored = favored;
	}

	public List<IpetComment> getComments() {
		return comments;
	}

	public void setComments(List<IpetComment> comments) {
		this.comments = comments;
	}

}
