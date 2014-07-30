package net.ipetty.android.comment;

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
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.DeleteComment;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.UserVO;

public class CommentAdapter extends BaseAdapter implements OnScrollListener {

	public final static String TAG = "CommentAdapter";
	private LayoutInflater inflater;
	private Context context;
	DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private List<CommentVO> list = new ArrayList<CommentVO>(); // 这个就本地dataStore

	public List<ModDialogItem> more_items;
	private ModDialogItem delItems;
	private Dialog moreDialog;
	private int currentClickItemPosition;

	public CommentAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		more_items = new ArrayList<ModDialogItem>();
		delItems = new ModDialogItem(null, context.getResources().getString(R.string.item_delete), delOnClick);
	}
	private OnClickListener delOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final CommentVO comment = list.get(CommentAdapter.this.currentClickItemPosition);
			Log.d(TAG, "commentID->" + comment.getId());
			new DeleteComment((Activity) context).setListener(new DefaultTaskListener<Boolean>((Activity) context, "正在删除...") {
				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						CommentAdapter.this.getList().remove(CommentAdapter.this.currentClickItemPosition);
						CommentAdapter.this.notifyDataSetChanged();
						Intent intent = new Intent(Constant.BROADCAST_INTENT_CCOMMENT_DELETE);
						intent.putExtra(Constant.CCOMMENT_ID, comment.getId());
						CommentAdapter.this.context.sendBroadcast(intent);
					}
				}
			}).execute(comment.getId());

			moreDialog.cancel();
		}
	};

	private class ViewOnLongClickListener implements OnLongClickListener {

		private int position;

		public ViewOnLongClickListener(int position) {
			this.position = position;
		}

		@Override
		public boolean onLongClick(View v) {
			CommentAdapter.this.currentClickItemPosition = position;
			CommentAdapter.this.showItems(position);
			return false;
		}
	};

	private void showItems(int position) {
		CommentVO comment = list.get(position);
		if (comment.getCreatedBy() == this.getCurrUserId()) {
			more_items.clear();
			more_items.add(delItems);
			moreDialog = DialogUtils.modPopupDialog(context, more_items, moreDialog);
		}
	}

	// 获取当前用户id
	private int getCurrUserId() {
		IpetApi api = IpetApi.init(this.context);
		return api.getCurrUserId();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	// 构建一个布局缓存的结构体 与VO对应
	public class ViewHolder {

		public TextView text;
		public TextView timestamp;
		public ImageView avatar;
	}

	private ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "list position-->" + position);
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			Log.d(TAG, "init items View");
			view = inflater.inflate(R.layout.list_comment_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.text);
			holder.timestamp = (TextView) view.findViewById(R.id.timestamp);
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 长按按钮
		view.setOnLongClickListener(new ViewOnLongClickListener(position));

		CommentVO commentVo = list.get(position);
		String creatAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentVo.getCreatedOn());
		holder.timestamp.setText(creatAt);

		int userId = commentVo.getCreatedBy();
		UserVO user = UserApiWithCache.getUserById4Synchronous(context, userId);

		//头像
		if (null != user.getAvatar()) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(),
					holder.avatar, options);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}

		String nickname = user.getNickname();
		String html = "<b><a href='" + userId + "'>" + nickname + "</a></b>";
		String text = commentVo.getText();
		html = html + " : " + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) context, holder.text, html);

		return view;
	}

	// 异步加载
	public void asynLoadUserInfo(Activity activity, final ViewHolder holder, final CommentVO commentVo) {
		int userId = commentVo.getCreatedBy();

		new GetUserById(activity).setListener(new DefaultTaskListener<UserVO>(activity) {
			@Override
			public void onSuccess(UserVO result) {
				String nickName = result.getNickname() == null ? "无名氏" : result.getNickname();
				String username = "<b><a href='1'>[" + nickName + "]</a></b>";
				String text = commentVo.getText();
				String html = username + ":" + text;
				WebLinkUtils.setUserLinkClickIntercept((Activity) context, holder.text, html);

				if (null != result.getAvatar()) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + result.getAvatar(),
							holder.avatar, options);
				} else {
					holder.avatar.setImageResource(R.drawable.avatar);
				}

			}
		}).execute(userId);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public List<CommentVO> getList() {
		return list;
	}

	public void setList(List<CommentVO> list) {
		this.list = list;
	}

}
