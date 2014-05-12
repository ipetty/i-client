package net.ipetty.android.sdk;

/**
 * IpetApi 门面
 * 
 * @author xiaojinghai
 */
public interface IpetApi {

	/**
	 * 评论API
	 */
	public CommentApi getCommentApi();

	/**
	 * 赞API
	 */
	public FavorApi getFavorApi();

	/**
	 * 发现API
	 */
	public DiscoverApi getDiscoverApi();

	/**
	 * 返回帐号API.
	 */
	public AccountApi getAccountApi();

	/**
	 * 返回用户API.
	 */
	public UserApi getUserApi();

	/**
	 * 返回用户关系API.
	 */
	public ContactApi getContactApi();

	/**
	 * 返回图片API
	 */
	public PhotoApi getPhotoApi();

	/**
	 * 返回意见反馈API
	 */
	public FeedbackApi getFeedbackApi();

	/**
	 * 返回应用API
	 */
	public AppApi getAppApi();

	/**
	 * 获取当前用户Id
	 */
	public String getCurrUserId();

}
