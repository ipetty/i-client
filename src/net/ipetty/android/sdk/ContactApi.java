package net.ipetty.android.sdk;

import java.util.List;

import net.ipetty.android.sdk.domain.IpetUser;

/**
 * 用户关系API
 * 
 * @author xiaojinghai
 */
public interface ContactApi {

	/**
	 * 获取我关注的人
	 */
	public List<IpetUser> getFollowList();

	/**
	 * 获取关注我的人
	 */
	public List<IpetUser> getFollowerList();

	/**
	 * 获取我的好友列表
	 */
	public List<IpetUser> getFriendList();

	/**
	 * 关注
	 */
	public Boolean follow(String userId);

	/**
	 * 反关注
	 */
	public Boolean unfollow(String userId);

}
