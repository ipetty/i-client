package net.ipetty.android.feed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.comment.CommentActivity;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.home.LargerImageActivity;
import net.ipetty.android.like.LikeActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.Favor;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SimpleFeedActivity extends Activity {
	public final static String TAG = SimpleFeedActivity.class.getSimpleName();
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private Long feedId = null;
	private FeedVO feed = null;

	public ImageView avatar;
	public TextView nickname;
	public TextView created_at;
	public TextView content;
	public ImageView content_image;
	public TextView address;

	public ImageView btn_liked;
	public TextView liked_detail;
	public ImageView btn_comment;
	public View btn_more;

	public TextView comments_num;
	public LinearLayout comments_group_list;

	public View feed_list_father_view;
	public View row_feed_photo_likes_group;
	public View feed_list_line;

	public List<ModDialogItem> more_items;
	private ModDialogItem shareItems;
	private ModDialogItem delItems;
	private Dialog moreDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_feed);

		Log.i(TAG, "onCreate");
		feedId = this.getIntent().getExtras().getLong(Constant.INTENT_FEED_ID_KEY);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_simple_feed));
		btnBack.setOnClickListener(new BackClickListener(this));

		more_items = new ArrayList<ModDialogItem>();
		shareItems = new ModDialogItem(null, getResources().getString(R.string.item_share), shareOnClick);
		delItems = new ModDialogItem(null, getResources().getString(R.string.item_delete), delOnClick);

		initView();
	}

	private void initView() {
		// 头像
		avatar = (ImageView) this.findViewById(R.id.avatar);

		// 姓名
		nickname = (TextView) this.findViewById(R.id.nickname);

		// 时间
		created_at = (TextView) this.findViewById(R.id.created_at);
		// 主题内容
		content = (TextView) this.findViewById(R.id.content);
		content_image = (ImageView) this.findViewById(R.id.content_image);
		// 地理位置
		address = (TextView) this.findViewById(R.id.address);

		// 操作区域
		btn_liked = (ImageView) this.findViewById(R.id.feed_button_like);
		btn_comment = (ImageView) this.findViewById(R.id.feed_button_comment);
		liked_detail = (TextView) this.findViewById(R.id.row_feed_photo_textview_likes);
		btn_more = this.findViewById(R.id.feed_button_more);

		feed_list_father_view = this.findViewById(R.id.feed_list_father_view);

		// like
		row_feed_photo_likes_group = this.findViewById(R.id.row_feed_photo_likes_group);
		feed_list_line = this.findViewById(R.id.feed_list_line);

		// 评论
		comments_num = (TextView) this.findViewById(R.id.row_feed_photo_textview_comments_num);
		comments_group_list = (LinearLayout) this.findViewById(R.id.row_feed_photo_comments_list);

		View view = this.findViewById(R.id.list_feed_item);
		view.setOnLongClickListener(new ViewOnLongClickListener());

		// 初始化界面操作
		initDefaultView();
	}

	private class ViewOnLongClickListener implements OnLongClickListener {
		@Override
		public boolean onLongClick(View v) {
			SimpleFeedActivity.this.showItems();
			return false;
		}
	};

	private int getCurrUserId() {
		IpetApi api = IpetApi.init(this);
		return api.getCurrUserId();
	}

	private void showItems() {
		more_items.clear();
		more_items.add(shareItems);
		// TODO: 需要增加一个是否有删除权限接口或者方法
		// 如果是当前用户 增加删除按钮
		if (feed.getCreatedBy() == this.getCurrUserId()) {
			more_items.add(delItems);
		}
		moreDialog = DialogUtils.modPopupDialog(this, more_items, moreDialog);
	}

	private OnClickListener shareOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO: 用户分享操作
			Toast.makeText(SimpleFeedActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "feedID->" + feedId);
			moreDialog.cancel();
		}
	};

	private OnClickListener delOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO: 用户删除操作
			Toast.makeText(SimpleFeedActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "feedID->" + feedId);
			moreDialog.cancel();
		}
	};

	private void initDefaultView() {
		// TODO Auto-generated method stub
		final FeedVO feed = this.feed;

		// 用户信息
		final UserVO user = this.getCacheUserById(feed.getCreatedBy());
		Log.d(TAG, "发布人头像：" + user.getAvatar());
		if (!StringUtils.isEmpty(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		}
		Log.d(TAG, "发布人昵称：" + user.getNickname());
		if (!StringUtils.isEmpty(user.getNickname())) {
			nickname.setText(user.getNickname());
		}
		// 头像
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SimpleFeedActivity.this, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				SimpleFeedActivity.this.startActivity(intent);
			}
		});
		// 姓名
		nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SimpleFeedActivity.this, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				SimpleFeedActivity.this.startActivity(intent);
			}
		});

		// 发布时间
		String creatAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feed.getCreatedOn());
		// TODO: 日期需要处理为 多少分钟前 多少秒前
		created_at.setText(creatAt); // 发布时间

		// 发布内容
		content.setText(feed.getText());

		// 图片显示
		ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + feed.getImageSmallURL(), content_image,
				options);

		// 内容空不显示内容区域
		if (StringUtils.isEmpty(feed.getText())) {
			content.setVisibility(View.GONE);
		} else {
			content.setVisibility(View.VISIBLE);
		}

		// TODO: 地理位置如何显示
		// 地理位置
		if (StringUtils.isEmpty("")) {
			address.setVisibility(View.GONE);
		} else {
			address.setVisibility(View.VISIBLE);
		}

		// TODO 代码可以优化，可以传递2个图片地址或者直接传递feedID
		// 转入默认看到的是小图，然后慢慢加载大图，有效防止网络不佳加载不出的情况。
		// 查看大图
		content_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示大图
				Intent intent = new Intent(SimpleFeedActivity.this, LargerImageActivity.class);
				intent.putExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY,
						Constant.FILE_SERVER_BASE + feed.getImageOriginalURL());
				SimpleFeedActivity.this.startActivity(intent);
			}
		});

		// 赞操作
		btn_liked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedFavorVO ffvo = new FeedFavorVO();
				ffvo.setFeedId(feed.getId());

				new Favor(SimpleFeedActivity.this).setListener(
						new DefaultTaskListener<FeedVO>(SimpleFeedActivity.this) {
							@Override
							public void onSuccess(FeedVO result) {
								// FeedAdapter.this.notifyDataSetChanged();
								// TODO: 这里没看懂上面怎样更新liked部分图形的
								// 重新增加对Favor变化操作 2014/6/21
								SimpleFeedActivity.this.updateFavor(result);
							}
						}).execute(ffvo);
			}
		});

		// 查看like
		liked_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SimpleFeedActivity.this, LikeActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, feed.getId());
				SimpleFeedActivity.this.startActivity(intent);
			}
		});

		// 评论
		OnClickListener myCommentClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SimpleFeedActivity.this, CommentActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, feed.getId());
				SimpleFeedActivity.this.startActivity(intent);
			}
		};
		btn_comment.setOnClickListener(myCommentClick);
		comments_num.setOnClickListener(myCommentClick);

		// 操作区域更多按钮
		btn_more.setOnClickListener(new MoreOperateOnClickListener());

		// 评论视图区域
		renderArea();
		renderFavor();
		renderCommentView(feed);

	}

	private void renderArea() {
		if (feed.getCommentCount() == 0 && feed.getFavorCount() == 0) {
			feed_list_father_view.setVisibility(View.GONE);
		} else {
			feed_list_father_view.setVisibility(View.VISIBLE);
		}

		if (feed.getCommentCount() > 0 && feed.getFavorCount() > 0) {
			feed_list_line.setVisibility(View.VISIBLE);
		} else {
			feed_list_line.setVisibility(View.GONE);
		}

		// 赞
		if (feed.getFavorCount() == 0) {
			row_feed_photo_likes_group.setVisibility(View.GONE);
		} else {
			row_feed_photo_likes_group.setVisibility(View.VISIBLE);
		}

		if (feed.getCommentCount() > 0) {
			comments_num.setVisibility(View.VISIBLE);
			comments_group_list.setVisibility(View.VISIBLE);
		} else {
			comments_num.setVisibility(View.GONE);
			comments_group_list.setVisibility(View.GONE);
		}

	}

	private void renderFavor() {
		if (feed.isFavored()) {
			btn_liked.setBackgroundResource(R.drawable.feed_button_like_active);
		} else {
			btn_liked.setBackgroundResource(R.drawable.feed_button_like_background);
		}
		renderFavorUserView(liked_detail, feed);
	}

	// public Map<Integer, UserVO> cacheUserMap = new HashMap<Integer,
	// UserVO>();
	private UserVO getCacheUserById(Integer id) {
		Log.d(TAG, "getUSER-->" + id);
		return UserApiWithCache.getUserById4Synchronous(this, id);
	}

	public void renderFavorUserView(TextView tv, FeedVO feedVO) {
		int currUserId = IpetApi.init(this).getCurrUserId();
		StringBuilder html = new StringBuilder("<b>");
		for (FeedFavorVO feedFavorVO : feedVO.getFavors()) {
			int id = feedFavorVO.getCreatedBy();
			String nickname = this.getCacheUserById(id).getNickname();
			if (id == currUserId) {
				nickname = "我";
			}
			html.append("<a href='").append(id).append("'>").append(nickname).append("</a> ");
		}
		html.append("</b>");
		String liked_num_text = this.getResources().getString(R.string.liked_num_text);
		Integer likedNum = feedVO.getFavorCount();
		html.append(String.format(liked_num_text, likedNum));
		Log.d(TAG, html.toString());
		WebLinkUtils.setUserLinkIntercept(this, tv, html.toString());
	}

	public void renderCommentView(FeedVO feedVO) {
		// 评论 总数
		String commentNumStr = this.getResources().getString(R.string.comment_num_text);
		Integer commentsNum = feedVO.getCommentCount();
		comments_num.setText(String.format(commentNumStr, commentsNum));

		comments_group_list.removeAllViews();
		for (CommentVO cvo : feedVO.getComments()) {
			comments_group_list.addView(addComment(cvo));
		}

	}

	private RelativeLayout addComment(CommentVO commentVO) {
		int id = commentVO.getCreatedBy();
		String nickname = this.getCacheUserById(id).getNickname();

		String html = "<b><a href='" + id + "'>" + nickname + "</a></b>";
		String text = commentVO.getText();
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.list_feed_item_feedback_comment, null);
		TextView t = (TextView) layout.findViewById(R.id.row_feed_textview_comments_item);
		html = html + " : " + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) this, t, html);
		return layout;
	}

	private class MoreOperateOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			showItems();
		}
	};

	// updateFavor
	public void updateFavor(FeedVO result) {
		// TODO Auto-generated method stub
		feed.setFavorCount(result.getFavorCount());
		feed.setFavored(result.isFavored());
		feed.setFavors(result.getFavors());

		renderArea();
		renderFavor();
	}

}
