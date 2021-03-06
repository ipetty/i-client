package net.ipetty.android.home;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.comment.CommentActivity;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.core.util.PrettyDateFormat;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.feed.SimpleFeedActivity;
import net.ipetty.android.like.LikeActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.DeleteFeed;
import net.ipetty.android.sdk.task.feed.Favor;
import net.ipetty.android.sdk.task.feed.GetFeedById;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.sharesdk.onekeyshare.OneKeyShare;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedFavorVO;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedAdapter extends BaseAdapter implements OnScrollListener {

	private String TAG = getClass().getSimpleName();

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private LayoutInflater inflater;
	private Context context;
	public List<ModDialogItem> more_items;
	private List<FeedVO> list = new ArrayList<FeedVO>(0); // 这个就本地dataStore
	private ModDialogItem shareItems;
	private ModDialogItem delItems;
	private Dialog moreDialog;
	private int currentPosition;

	private OneKeyShare oks;

	public FeedAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		more_items = new ArrayList<ModDialogItem>();
		shareItems = new ModDialogItem(null, context.getResources().getString(R.string.item_share), shareOnClick);
		delItems = new ModDialogItem(null, context.getResources().getString(R.string.item_delete), delOnClick);

		this.oks = new OneKeyShare(context);
	}

	@Override
	public int getCount() {
		Log.d(TAG, "getCount" + list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "getItem:" + position);
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		FeedVO feed = list.get(position);
		Log.d(TAG, "getItemId" + feed.getId());
		return feed.getId();
	}

	// 获取当前用户id
	private int getCurrUserId() {
		IpetApi api = IpetApi.init(this.context);
		return api.getCurrUserId();
	}

	public List<FeedVO> getList() {
		return list;
	}

	// 删除Feed
	public void removeById(long feedId) {
		int position = -1;
		int i = 0;
		for (FeedVO feed : list) {
			if (feed.getId() == feedId) {
				position = i;
			}
			i++;
		}
		if (position != -1) {
			list.remove(position);
		}
	}

	// 删除评论
	public void removeCommentById(long commentId) {
		int position = -1;

		for (FeedVO feed : list) {
			List<CommentVO> commentVOList = feed.getComments();
			int i = 0;
			for (CommentVO comment : commentVOList) {
				if (commentId == comment.getId()) {
					position = i;
					break;
				}
				i++;
			}
			if (position != -1) {
				commentVOList.remove(position);
				this.notifyDataSetChanged();
				break;
			}
		}

	}

	public void setList(List<FeedVO> list) {
		this.list = list;
	}

	public int getPosItemById(Long id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	// 构建一个布局缓存的结构体 与VO对应
	public class ViewHolder {

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

	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView " + position + " " + convertView);

		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			Log.d(TAG, "init items View");
			view = inflater.inflate(R.layout.list_feed_item, null);
			holder = new ViewHolder();

			// 头像
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);

			// 姓名
			holder.nickname = (TextView) view.findViewById(R.id.nickname);

			// 时间
			holder.created_at = (TextView) view.findViewById(R.id.created_at);
			// 主题内容
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.content_image = (ImageView) view.findViewById(R.id.content_image);
			// 地理位置
			holder.address = (TextView) view.findViewById(R.id.address);

			// 操作区域
			holder.btn_liked = (ImageView) view.findViewById(R.id.feed_button_like);
			holder.btn_comment = (ImageView) view.findViewById(R.id.feed_button_comment);
			holder.liked_detail = (TextView) view.findViewById(R.id.row_feed_photo_textview_likes);
			holder.btn_more = view.findViewById(R.id.feed_button_more);

			holder.feed_list_father_view = view.findViewById(R.id.feed_list_father_view);

			// like
			holder.row_feed_photo_likes_group = view.findViewById(R.id.row_feed_photo_likes_group);
			holder.feed_list_line = view.findViewById(R.id.feed_list_line);

			// 评论
			holder.comments_num = (TextView) view.findViewById(R.id.row_feed_photo_textview_comments_num);
			holder.comments_group_list = (LinearLayout) view.findViewById(R.id.row_feed_photo_comments_list);

			convertView = view;
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();// 取出ViewHolder对象
		}

		// 长按按钮
		view.setOnLongClickListener(new ViewOnLongClickListener(position));

		// 初始化界面操作
		initDefaultView(holder, position);

		// FeedVO feed = list.get(position);
		// asynLoad((Activity) context, holder, feed);
		return view;
	}

	// 长按菜单出现
	private int currentClickItemPosition;

	private class ViewOnLongClickListener implements OnLongClickListener {

		private int position;

		public ViewOnLongClickListener(int position) {
			this.position = position;
		}

		@Override
		public boolean onLongClick(View v) {
			FeedAdapter.this.showItems(position);
			FeedAdapter.this.currentClickItemPosition = position;
			return false;
		}
	};

	private void showItems(int position) {
		more_items.clear();
		more_items.add(shareItems);
		FeedVO feed = (FeedVO) FeedAdapter.this.getItem(position);
		// TODO: 需要增加一个是否有删除权限接口或者方法
		// 如果是当前用户 增加删除按钮
		if (feed.getCreatedBy() == this.getCurrUserId()) {
			more_items.add(delItems);
		}
		moreDialog = DialogUtils.modPopupDialog(context, more_items, moreDialog);
	}

	private OnClickListener shareOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FeedVO feed = (FeedVO) FeedAdapter.this.getItem(FeedAdapter.this.currentClickItemPosition);
			Log.d(TAG, "分享消息：feedId=" + feed.getId());
			UserVO user = UserApiWithCache.getUserById4Synchronous(context, feed.getCreatedBy());
			String feedAuthor = user.getNickname();
			String feedBody = feed.getText();
			String imageUri = feed.getImageOriginalURL();
			String imageUrl = StringUtils.isNotBlank(imageUri) ? Constant.FILE_SERVER_BASE + imageUri : null;

			oks.share(feedAuthor, feedBody, imageUrl);
			moreDialog.cancel();
		}
	};

	private OnClickListener delOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final long feedId = FeedAdapter.this.getItemId(FeedAdapter.this.currentClickItemPosition);
			Log.d(TAG, "删除消息：feedId=" + feedId);
			new DeleteFeed((Activity) context).setListener(
					new DefaultTaskListener<Boolean>((Activity) context, "正在删除...") {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Intent intent = new Intent(Constant.BROADCAST_INTENT_FEED_DELETE);
								intent.putExtra(Constant.FEEDVO_ID, feedId);
								FeedAdapter.this.context.sendBroadcast(intent);
								// FeedAdapter.this.getList().remove(FeedAdapter.this.currentClickItemPosition);
								// FeedAdapter.this.notifyDataSetChanged();
							}
						}
					}).execute(feedId);

			moreDialog.cancel();
		}
	};

	// ---------------------- end
	// 操作区域更多按钮
	private class MoreOperateOnClickListener implements OnClickListener {

		private int position;

		public MoreOperateOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			FeedAdapter.this.showItems(position);
			FeedAdapter.this.currentClickItemPosition = position;
		}
	};

	// ------------------------end
	private void initDefaultView(ViewHolder holder, int position) {
		Log.d(TAG, "--initDefaultView--");

		final FeedVO feed = (FeedVO) this.getItem(position);

		// 用户信息
		final UserVO user = this.getCacheUserById(feed.getCreatedBy());
		Log.d(TAG, "发布人头像：" + user.getAvatar());
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance()
					.displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), holder.avatar, options);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}
		Log.d(TAG, "发布人昵称：" + user.getNickname());
		if (StringUtils.isNotBlank(user.getNickname())) {
			holder.nickname.setText(user.getNickname());
		}
		// 头像
		holder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				// intent.putExtra("uid", user.getUid());
				// intent.putExtra("uniqueName", user.getUniqueName());
				context.startActivity(intent);
			}
		});
		// 姓名
		holder.nickname.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SpaceActivity.class);
				intent.putExtra(Constant.INTENT_USER_ID_KEY, user.getId());
				// intent.putExtra("uid", user.getUid());
				// intent.putExtra("uniqueName", user.getUniqueName());
				context.startActivity(intent);
			}
		});

		// 发布时间
		// String creatAt = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feed.getCreatedOn());
		// 日期需要处理为 多少分钟前 多少秒前
		String creatAt = new PrettyDateFormat("@", "yyyy-MM-dd HH:mm:dd").format(feed.getCreatedOn());
		holder.created_at.setText(creatAt); // 发布时间
		holder.created_at.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SimpleFeedActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, feed.getId());
				intent.putExtra(Constant.FEEDVO_JSON_SERIALIZABLE, JSONUtils.toJson(feed).toString());
				context.startActivity(intent);
			}
		});

		// 发布内容
		holder.content.setText(feed.getText());

		// 图片显示
		if (StringUtils.isNotBlank(feed.getImageSmallURL())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + feed.getImageSmallURL(),
					holder.content_image, options);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}

		// 内容空不显示内容区域
		if (StringUtils.isBlank(feed.getText())) {
			holder.content.setVisibility(View.GONE);
		} else {
			holder.content.setVisibility(View.VISIBLE);
		}

		// 地理位置
		if (feed.getLocation() == null || StringUtils.isBlank(feed.getLocation().getAddress())) {
			holder.address.setVisibility(View.GONE);
		} else {
			holder.address.setVisibility(View.VISIBLE);
			holder.address.setText(feed.getLocation().getAddress());
		}

		// TODO 代码可以优化，可以传递2个图片地址或者直接传递feedID
		// 转入默认看到的是小图，然后慢慢加载大图，有效防止网络不佳加载不出的情况。
		// 查看大图
		holder.content_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 展示大图
				Intent intent = new Intent(context, LargerImageActivity.class);
				intent.putExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY,
						Constant.FILE_SERVER_BASE + feed.getImageOriginalURL());
				intent.putExtra(Constant.INTENT_IMAGE_SAMILL_KEY, Constant.FILE_SERVER_BASE + feed.getImageSmallURL());
				context.startActivity(intent);
			}
		});

		// 赞操作
		holder.btn_liked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedFavorVO ffvo = new FeedFavorVO();
				ffvo.setFeedId(feed.getId());

				new Favor((Activity) context).setListener(new DefaultTaskListener<FeedVO>((Activity) context) {
					@Override
					public void onSuccess(FeedVO result) {
						// FeedAdapter.this.notifyDataSetChanged();
						// TODO: 这里没看懂上面怎样更新liked部分图形的
						// 重新增加对Favor变化操作 2014/6/21
						FeedAdapter.this.updateFavor(result);
					}
				}).execute(ffvo);
			}
		});

		// 查看like
		holder.liked_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LikeActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, feed.getId());
				context.startActivity(intent);
			}
		});

		// 评论
		OnClickListener myCommentClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommentActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, feed.getId());
				context.startActivity(intent);
			}
		};
		holder.btn_comment.setOnClickListener(myCommentClick);
		holder.comments_num.setOnClickListener(myCommentClick);

		// 操作区域更多按钮
		holder.btn_more.setOnClickListener(new MoreOperateOnClickListener(position));

		// 评论视图区域
		if (feed.getCommentCount() == 0 && feed.getFavorCount() == 0) {
			holder.feed_list_father_view.setVisibility(View.GONE);
		} else {
			holder.feed_list_father_view.setVisibility(View.VISIBLE);
		}

		if (feed.getCommentCount() > 0 && feed.getFavorCount() > 0) {
			holder.feed_list_line.setVisibility(View.VISIBLE);
		} else {
			holder.feed_list_line.setVisibility(View.GONE);
		}

		if (feed.getCommentCount() > 0) {
			holder.comments_num.setVisibility(View.VISIBLE);
			holder.comments_group_list.setVisibility(View.VISIBLE);
		} else {
			holder.comments_num.setVisibility(View.GONE);
			holder.comments_group_list.setVisibility(View.GONE);
		}

		// 赞
		if (feed.getFavorCount() == 0) {
			holder.row_feed_photo_likes_group.setVisibility(View.GONE);
		} else {
			holder.row_feed_photo_likes_group.setVisibility(View.VISIBLE);
		}

		if (feed.isFavored()) {
			holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_active);
		} else {
			holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_background);
		}

		renderFavorUserView(holder.liked_detail, feed);
		renderCommentView(holder, feed);

	}

	// public Map<Integer, UserVO> cacheUserMap = new HashMap<Integer,
	// UserVO>();
	private UserVO getCacheUserById(Integer id) {
		Log.d(TAG, "getUSER-->" + id);
		/*
		 * UserVO user = new UserVO();
		 * user.setAvatar("/files/10161/ls1h6zdbyv7lf.jpg");
		 * user.setNickname("TEST");
		 */
		long a = System.currentTimeMillis();// 在最好的一行加上:
		UserVO user = UserApiWithCache.getUserById4Synchronous(context, id);
		System.out.println("UserApiWithCache 执行耗时 : " + (System.currentTimeMillis() - a) + " ms ");

		return user;
		// TODO: 这里代码有问题~没有办法直接调用
		// user = IpetApi.init((MainActivity) context).getUserApi().getById(id);

		// UserVO user = cacheUserMap.get(id);
		// if (user == null) {
		// user = IpetApi.init((MainActivity) context).getUserApi().getById(id);
		// cacheUserMap.put(user.getId(), user);
		//
		// //TODO：有点变态的改写方法，不知道这样大量的异步同步刷新界面，会不会导致界面卡死
		// // new GetUserById((Activity) context).setListener(new
		// DefaultTaskListener<UserVO>((Activity) context) {
		// //
		// // @Override
		// // public void onSuccess(UserVO result) {
		// // // TODO Auto-generated method stub
		// // if (FeedAdapter.this.cacheUserMap.containsKey(result.getId())) {
		// // return;
		// // }
		// // FeedAdapter.this.cacheUserMap.put(result.getId(), result);
		// // FeedAdapter.this.notifyDataSetChanged();
		// // }
		// //
		// // }).execute(id);
		// }
		// return user;
	}

	public void renderFavorUserView(TextView tv, FeedVO feedVO) {
		int currUserId = IpetApi.init(context).getCurrUserId();
		StringBuilder html = new StringBuilder("<b>");
		int lastIndex = feedVO.getFavors().size() - 1;
		for (FeedFavorVO feedFavorVO : feedVO.getFavors()) {
			int id = feedFavorVO.getCreatedBy();
			String nickname = this.getCacheUserById(id).getNickname();
			if (id == currUserId) {
				nickname = "我";
			}
			html.append("<a href='").append(id).append("'>").append(nickname).append("</a>");
			if (feedVO.getFavors().indexOf(feedFavorVO) != lastIndex) {
				html.append("、");
			}
		}
		html.append("</b>");
		String liked_num_text = context.getResources().getString(R.string.liked_num_text);
		Integer likedNum = feedVO.getFavorCount();
		html.append(String.format(liked_num_text, likedNum));
		Log.d(TAG, html.toString());
		WebLinkUtils.setUserLinkIntercept((Activity) context, tv, html.toString());
	}

	public void renderCommentView(ViewHolder holder, FeedVO feedVO) {
		// 评论 总数
		String commentNumStr = context.getResources().getString(R.string.comment_num_text);
		Integer commentsNum = feedVO.getComments().size();
		holder.comments_num.setText(String.format(commentNumStr, commentsNum));

		holder.comments_group_list.removeAllViews();
		for (CommentVO cvo : feedVO.getComments()) {
			holder.comments_group_list.addView(addComment(cvo));
		}

	}

	private RelativeLayout addComment(CommentVO commentVO) {
		int id = commentVO.getCreatedBy();
		String nickname = this.getCacheUserById(id).getNickname();

		String html = "<b><a href='" + id + "'>" + nickname + "</a></b>";
		String text = commentVO.getText();
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.list_feed_item_feedback_comment, null);
		TextView t = (TextView) layout.findViewById(R.id.row_feed_textview_comments_item);
		html = html + " : " + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) context, t, html);
		return layout;
	}

	// updateFavor
	public void updateFavor(FeedVO result) {
		// TODO Auto-generated method stub
		int i = this.getPosItemById(result.getId());
		if (i == -1) {
			return;
		}
		FeedVO feed = this.list.get(i);
		feed.setFavorCount(result.getFavorCount());
		feed.setFavored(result.isFavored());
		feed.setFavors(result.getFavors());
		this.notifyDataSetChanged();
	}

	public void updateFeedVO(FeedVO feedVO) {
		// TODO Auto-generated method stub
		int i = this.getPosItemById(feedVO.getId());
		if (i == -1) {
			return;
		}
		this.list.set(i, feedVO);
		this.notifyDataSetChanged();
	}

	public void prependFeedVO(FeedVO feedVO) {
		// TODO Auto-generated method stub
		this.list.add(0, feedVO);
		this.notifyDataSetChanged();
	}

	// TODO: 这里的代码暂时不使用~
	// 需要异步加载的内容
	public void asynLoad(final Activity activity, final ViewHolder holder, final FeedVO vo) {
		int userId = vo.getCreatedBy();

		// 异步加载发布人信息
		new GetUserById(activity).setListener(new DefaultTaskListener<UserVO>(activity) {

			@Override
			public void onSuccess(final UserVO resultUser) {
				// 头像
				holder.avatar.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, SpaceActivity.class);
						intent.putExtra("id", resultUser.getId());
						context.startActivity(intent);
					}
				});
				// 姓名
				holder.nickname.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, SpaceActivity.class);
						intent.putExtra("id", resultUser.getId());
						context.startActivity(intent);
					}
				});

				// 发布人信息
				Log.d(TAG, "发布人头像：" + resultUser.getAvatar());
				if (StringUtils.isNotBlank(resultUser.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + resultUser.getAvatar(),
							holder.avatar, options);
				} else {
					holder.avatar.setImageResource(R.drawable.avatar);
				}
				Log.d(TAG, "发布人昵称：" + resultUser.getNickname());
				if (StringUtils.isNotBlank(resultUser.getNickname())) {
					holder.nickname.setText(resultUser.getNickname());
				}

				// 评论和赞
				new GetFeedById(activity).setListener(new DefaultTaskListener<FeedVO>(activity) {

					@Override
					public void onSuccess(FeedVO result) {
						// 赞
						String html = "<b><a href='1'>张三</a>,<a href='2'>李四四</a>,<a href='3'>王五</a></b>";
						StringBuilder sb = new StringBuilder("<b>");
						int i = 0;
						boolean isFavored = false;
						int currUserId = IpetApi.init(context).getCurrUserId();
						for (FeedFavorVO fvo : result.getFavors()) {
							// TODO：这里的 resultUser 是当前用户 没有遍历 favors中的用户
							String nickName = resultUser.getNickname() == null ? "无名氏" : "";
							if (i < 2) {
								sb.append("<a href='").append(i).append("'>").append(nickName).append("</a> ");
							}
							if (currUserId == fvo.getCreatedBy()) {
								isFavored = true;
							}
							i++;
						}
						sb.append("</b>");

						String liked_num_text = activity.getResources().getString(R.string.liked_num_text);
						Integer likedNum = result.getFavors().size();
						sb.append(String.format(liked_num_text, likedNum));
						WebLinkUtils.setUserLinkIntercept(activity, holder.liked_detail, sb.toString());

						// 是否赞过的图标
						if (isFavored) {
							holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_active);
						} else {
							holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_background);
						}

						// 评论 总数
						String commentNumStr = activity.getResources().getString(R.string.comment_num_text);
						Integer commentsNum = result.getComments().size();
						holder.comments_num.setText(String.format(commentNumStr, commentsNum));

						holder.comments_group_list.removeAllViews();
						for (CommentVO cvo : result.getComments()) {
							// TODO：这里的 resultUser 是当前用户 没有遍历 Comment中的用户
							holder.comments_group_list.addView(addComment(cvo, resultUser));
						}

					}
				}).execute(vo.getId());
			}
		}).execute(userId);
	}

	private RelativeLayout addComment(CommentVO commentVO, UserVO userVO) {
		String nickName = userVO.getNickname() == null ? "无名氏" : userVO.getNickname();
		String username = "<b><a href='1'>" + nickName + "</a></b>";
		String text = commentVO.getText();

		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.list_feed_item_feedback_comment, null);
		TextView t = (TextView) layout.findViewById(R.id.row_feed_textview_comments_item);
		String html = username + " : " + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) context, t, html);

		return layout;
	}

	private OnClickListener userInfoOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, SpaceActivity.class);
			context.startActivity(intent);
		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

}
