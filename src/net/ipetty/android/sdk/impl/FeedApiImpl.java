package net.ipetty.android.sdk.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.DateUtils;
import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.FeedApi;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.FeedList;
import net.ipetty.vo.FeedTimelineQueryParams;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.FeedWithLocationVO;
import net.ipetty.vo.ImageVO;
import net.ipetty.vo.LocationVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;

import android.content.Context;

/**
 * FeedApiImpl
 * 
 * @author luocanfeng
 * @date 2014年5月12日
 */
public class FeedApiImpl extends ApiBase implements FeedApi {

	public FeedApiImpl(Context context) {
		super(context);
	}

	private static final String URI_PUBLISH_IMAGE = "/feed/publishImage";
	private static final String URI_PUBLISH_TEXT = "/feed/publishText";

	/**
	 * 发布消息
	 */
	@Override
	public FeedVO publish(FeedFormVO feed) {
		super.requireAuthorization();

		Long imageId = null;
		if (StringUtils.isNotBlank(feed.getImagePath())) { // 发布图片
			URI publishImageUri = buildUri(URI_PUBLISH_IMAGE);
			LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
			request.add("imageFile", new FileSystemResource(feed.getImagePath()));
			ImageVO image = getRestTemplate().postForObject(publishImageUri, request, ImageVO.class);
			imageId = image.getId();
		}

		URI publishTextUri = buildUri(URI_PUBLISH_TEXT);
		LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
		request.add("text", feed.getText());
		request.add("imageId", imageId == null ? null : String.valueOf(imageId));
		return getRestTemplate().postForObject(publishTextUri, request, FeedVO.class);
	}

	private static final String URI_PUBLISH_TEXT2 = "/feed/publishText2";

	/**
	 * 发布消息（带地理位置信息）
	 */
	@Override
	public FeedVO publishWithLocation(FeedFormVO feed) {
		super.requireAuthorization();

		Long imageId = null;
		if (StringUtils.isNotBlank(feed.getImagePath())) { // 发布图片
			URI publishImageUri = buildUri(URI_PUBLISH_IMAGE);
			LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
			request.add("imageFile", new FileSystemResource(feed.getImagePath()));
			ImageVO image = getRestTemplate().postForObject(publishImageUri, request, ImageVO.class);
			imageId = image.getId();
		}

		URI publishTextUri = buildUri(URI_PUBLISH_TEXT2);
		LocationVO location = feed.getLocation();
		FeedWithLocationVO feedWithLocationVO = new FeedWithLocationVO(feed.getText(), imageId, location);
		return getRestTemplate().postForObject(publishTextUri, feedWithLocationVO, FeedVO.class);
	}

	private static final String URI_GET_BY_ID = "/feed/{id}";

	/**
	 * 根据ID获取消息
	 */
	@Override
	public FeedVO getById(Long id) {
		return getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_GET_BY_ID, FeedVO.class, id);
	}

	private static final String URI_LIST_BY_TIMELINE_FOR_SQUARE = "/feed/square";

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<FeedVO> listByTimelineForSquare(Date timeline, int pageNumber, int pageSize) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("timeline", DateUtils.toDatetimeString(timeline));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_BY_TIMELINE_FOR_SQUARE, request);
		return Arrays.asList(getRestTemplate().getForObject(uri, FeedVO[].class));
	}

	private static final String URI_LIST_BY_TIMELINE_FOR_HOMEPAGE = "/feed/home";

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<FeedVO> listByTimelineForHomePage(Date timeline, int pageNumber, int pageSize) {
		super.requireAuthorization();

		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("timeline", DateUtils.toDatetimeString(timeline));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_BY_TIMELINE_FOR_HOMEPAGE, request);
		return Arrays.asList(getRestTemplate().getForObject(uri, FeedVO[].class));
	}

	private static final String URI_LIST_BY_TIMELINE_FOR_SPACE = "/feed/space";

	/**
	 * 根据时间线分页获取指定用户空间的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	@Override
	public List<FeedVO> listByTimelineForSpace(Integer userId, Date timeline, int pageNumber, int pageSize) {
		LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("userId", String.valueOf(userId));
		request.add("timeline", DateUtils.toDatetimeString(timeline));
		request.add("pageNumber", String.valueOf(pageNumber));
		request.add("pageSize", String.valueOf(pageSize));
		URI uri = buildUri(URI_LIST_BY_TIMELINE_FOR_SPACE, request);
		return Arrays.asList(getRestTemplate().getForObject(uri, FeedVO[].class));
	}

	// private static final String URI_LIST_BY_TIMELINE_FOR_SQUARE2 =
	// "/feed/square2";

	/**
	 * 根据时间线分页获取消息（广场）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForSquare(FeedTimelineQueryParams queryParams) {
		return getRestTemplate().postForObject(buildUri(URI_LIST_BY_TIMELINE_FOR_SQUARE), queryParams, FeedList.class);
	}

	/**
	 * 根据时间线分页获取消息（我和我关注人的）
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForHomePage(FeedTimelineQueryParams queryParams) {
		super.requireAuthorization();
		return getRestTemplate()
				.postForObject(buildUri(URI_LIST_BY_TIMELINE_FOR_HOMEPAGE), queryParams, FeedList.class);
	}

	/**
	 * 根据时间线分页获取指定用户空间的消息
	 * 
	 * @param pageNumber
	 *            分页页码，从0开始
	 */
	public FeedList listByTimelineForSpace(FeedTimelineQueryParams queryParams) {
		return getRestTemplate().postForObject(buildUri(URI_LIST_BY_TIMELINE_FOR_SPACE), queryParams, FeedList.class);
	}

	private static final String URI_LIST_COMMENTS_BY_FEED_ID = "/feed/comments/{feedId}";

	/**
	 * 获取指定消息的评论列表
	 */
	public List<CommentVO> listCommentsByFeedId(Long feedId) {
		return Arrays.asList(getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_LIST_COMMENTS_BY_FEED_ID,
				CommentVO[].class, feedId));
	}

	private static final String URI_LIST_FAVORS_BY_FEED_ID = "/feed/favors/{feedId}";

	/**
	 * 获取指定消息的赞列表
	 */
	public List<FeedFavorVO> listFavorsByFeedId(Long feedId) {
		return Arrays.asList(getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_LIST_FAVORS_BY_FEED_ID,
				FeedFavorVO[].class, feedId));
	}

	private static final String URI_COMMENT = "/feed/comment";

	/**
	 * 评论
	 */
	@Override
	public FeedVO comment(CommentVO comment) {
		super.requireAuthorization();

		return getRestTemplate().postForObject(buildUri(URI_COMMENT), comment, FeedVO.class);
	}

	private static final String URI_FAVOR = "/feed/favor";

	/**
	 * 赞
	 */
	@Override
	public FeedVO favor(FeedFavorVO favor) {
		super.requireAuthorization();

		return getRestTemplate().postForObject(buildUri(URI_FAVOR), favor, FeedVO.class);
	}

	private static final String URI_UNFAVOR = "/feed/unfavor";

	/**
	 * 取消赞
	 */
	@Override
	public FeedVO unfavor(FeedFavorVO favor) {
		super.requireAuthorization();

		return getRestTemplate().postForObject(buildUri(URI_UNFAVOR), favor, FeedVO.class);
	}

	private static final String URI_DELETE_BY_ID = "/feed/delete";

	/**
	 * 删除消息
	 */
	@Override
	public boolean delete(Long id) {
		super.requireAuthorization();

		return getRestTemplate().postForObject(buildUri(URI_DELETE_BY_ID, "id", String.valueOf(id)), null,
				Boolean.class);
	}

	private static final String URI_DELETE_COMMENT = "/feed/comment/delete";

	/**
	 * 删除评论
	 */
	@Override
	public boolean deleteComment(Long id) {
		super.requireAuthorization();

		return getRestTemplate().postForObject(buildUri(URI_DELETE_COMMENT, "id", String.valueOf(id)), null,
				Boolean.class);
	}

}
