package net.ipetty.android.sdk.domain;

import net.ipetty.android.sdk.base.ApiContext;

public class IpetUser {

	// @JsonDeserialize(as = Long.class)
	private String id;
	// 登录名称
	private String loginName;
	// 显示名称
	private String displayName;
	private String email;
	private String phone;
	// 32*32头像地址
	private String avatar32;
	// 48*48头像地址
	private String avatar48;
	// 登录次数
	private String loginNum;
	// 拥有店铺的数量
	private String shopCount;
	// 拥有APP的数量
	private String appCount;
	// 粉丝数量
	private String followerCount;
	// 互粉数量
	private String friendCount;
	// 关注数量
	private String followCount;
	// 发布图片的数量
	private String photoCount;
	// 赞别人的数量
	private String favorCount;
	// 发表评论数量
	private String commentCount;

	private String createAt;

	private String updateAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar32() {
		return ApiContext.FILE_SERVER_BASE + avatar32;
	}

	public void setAvatar32(String avatar32) {
		this.avatar32 = avatar32;
	}

	public String getAvatar48() {
		return ApiContext.FILE_SERVER_BASE + avatar48;
	}

	public void setAvatar48(String avatar48) {
		this.avatar48 = avatar48;
	}

	public String getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(String loginNum) {
		this.loginNum = loginNum;
	}

	public String getShopCount() {
		return shopCount;
	}

	public void setShopCount(String shopCount) {
		this.shopCount = shopCount;
	}

	public String getAppCount() {
		return appCount;
	}

	public void setAppCount(String appCount) {
		this.appCount = appCount;
	}

	public String getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(String followerCount) {
		this.followerCount = followerCount;
	}

	public String getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(String friendCount) {
		this.friendCount = friendCount;
	}

	public String getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(String photoCount) {
		this.photoCount = photoCount;
	}

	public String getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(String favorCount) {
		this.favorCount = favorCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getFollowCount() {
		return followCount;
	}

	public void setFollowCount(String followCount) {
		this.followCount = followCount;
	}

}
