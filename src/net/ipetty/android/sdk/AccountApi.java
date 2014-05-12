package net.ipetty.android.sdk;

import net.ipetty.android.sdk.domain.IpetUser;

/**
 * 帐号API
 * 
 * @author xiaojinghai
 */
public interface AccountApi {

	/**
	 * 登入
	 */
	public IpetUser login(String loginName, String password);

	/**
	 * 登出
	 */
	public void logout();

	/**
	 * 注册
	 */
	public IpetUser register(String loginName, String password);

	/**
	 * 检查登录名是否已存在
	 */
	public Boolean checkLoginName(String loginName);

	/**
	 * 检查Phone是否已存在
	 */
	public Boolean checkPhone(String phone);

	/**
	 * 检查Email是否已存在
	 */
	public Boolean checkEmail(String email);

	/**
	 * 修改密码
	 * 
	 * @param oldP
	 *            原密码
	 * @param newP
	 *            新密码
	 */
	public Boolean changePassword(String oldP, String newP);

}
