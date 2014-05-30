package net.ipetty.android.home;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.comment.CommentActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.vo.FeedVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class FeedAdapter extends BaseAdapter implements OnScrollListener {

	private DisplayImageOptions options;
	public final static String TAG = "FeedAdapter";
	private LayoutInflater inflater;
	private Context context;
	public List<ModDialogItem> more_items;
	private List list = null; // 这个就本地dataStore
	private ModDialogItem shareItems;
	private ModDialogItem delItems;
	private Dialog moreDialog;

	public FeedAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		options = AppUtils.getNormalImageOptions();
		more_items = new ArrayList<ModDialogItem>();

		shareItems = new ModDialogItem(null, "分享", shareOnClick);
		delItems = new ModDialogItem(null, "删除", delOnClick);

	}

	private OnClickListener shareOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			moreDialog.cancel();
		}
	};

	private OnClickListener delOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			moreDialog.cancel();
		}

	};

	@Override
	public int getCount() {
		return 2;// list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 构建一个布局缓存的结构体 与VO对应
	public class ViewHolder {
		public ImageView avator;
		public TextView nickname;
		public TextView created_at;
		public TextView content;
		public ImageView content_image;
		public TextView address;

		public ImageView btn_liked;
		public View liked_detail;
		public ImageView btn_comment;
		public View btn_more;

		public TextView like_text;

	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_feed_item, null);
			holder = new ViewHolder();

			view.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					showItems();

					return false;
				}

			});

			// 头像
			holder.avator = (ImageView) view.findViewById(R.id.avator);
			holder.avator.setOnClickListener(userInfoOnClick);
			// 姓名
			holder.nickname = (TextView) view.findViewById(R.id.nickname);
			holder.nickname.setOnClickListener(userInfoOnClick);
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
			holder.liked_detail = view.findViewById(R.id.row_feed_photo_textview_likes);
			holder.btn_more = view.findViewById(R.id.feed_button_more);
			holder.btn_more.setOnClickListener(moreOnClick);

			// like
			holder.like_text = (TextView) view.findViewById(R.id.row_feed_photo_textview_likes);

			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		FeedVO feed = new FeedVO();
		initDefaultView(holder, feed, position);
		initLikedBtnView(holder, feed, position);
		initCommentView(holder, feed, position);
		// 数据与界面绑定

		return view;
	}

	private void showItems() {
		// TODO Auto-generated method stub
		more_items.clear();
		more_items.add(shareItems);
		// TODO:按条件添加删除按钮

		moreDialog = DialogUtils.modPopupDialog(context, more_items, moreDialog);
	}

	private OnClickListener moreOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showItems();
		}
	};

	private void initDefaultView(ViewHolder holder, FeedVO feed, int position) {
		// TODO 基本内容信息修改
		// ImageLoader.getInstance().displayImage(url, holder.avator, options);
		// holder.nickname.setText(text);
		// holder.created_at.setText(text);
		// holder.content.setText(text);
		// 内容空不显示内容区域
		if (StringUtils.isEmpty("")) {
			holder.content.setVisibility(View.GONE);
		} else {
			holder.content.setVisibility(View.VISIBLE);
		}

		// 地理位置
		if (StringUtils.isEmpty("")) {
			holder.address.setVisibility(View.GONE);
		} else {
			holder.address.setVisibility(View.VISIBLE);
		}

		// ImageLoader.getInstance().displayImage(url, holder.content_image,
		// options);
		holder.content_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 展示大图
				// Intent intent = new Intent((MainActivity) context,
				// LargerImageActivity.class);
				// ((MainActivity) context).startActivity(intent);
				Toast.makeText(context, "暂未实现", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private OnClickListener userInfoOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "暂未实现", Toast.LENGTH_SHORT).show();
		}
	};

	private void initLikedBtnView(ViewHolder holder, final FeedVO feed, int position) {
		if (feed.isFavored()) {
			holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_active);
		} else {
			holder.btn_liked.setBackgroundResource(R.drawable.feed_button_like_background);
		}

		holder.liked_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent((MainActivity) context,
				// LikeActivity.class);
				// ((MainActivity) context).startActivity(intent);
			}
		});

		holder.btn_liked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initCommentView(ViewHolder holder, final FeedVO feed, final int position) {
		OnCommentClick myCommentClick = new OnCommentClick(feed);
		holder.btn_comment.setOnClickListener(myCommentClick);

		String html = "<b><a href='1'>张三</a>,<a href='2'>李四四</a>,<a href='3'>王五</a></b>";
		setLinkClickIntercept(holder.like_text, html);
	}

	private void setLinkClickIntercept(TextView tv, String text) {
		CharSequence charSequence = Html.fromHtml(text);
		Spannable sp = (Spannable) charSequence;
		int end = charSequence.length();
		URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
		Log.i(TAG, "length" + urls.length);
		SpannableStringBuilder spannable = new SpannableStringBuilder(sp);
		for (URLSpan url : urls) {
			Log.i(TAG, "url" + url.getURL());
			Log.i(TAG, "start" + sp.getSpanStart(url));
			Log.i(TAG, "end" + sp.getSpanEnd(url));
			spannable.setSpan(new MyURLSpan(), sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(spannable);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private class MyURLSpan extends ClickableSpan {
		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			Toast.makeText(context, "T", Toast.LENGTH_SHORT).show();
		}
	}

	public class OnCommentClick implements OnClickListener {
		private FeedVO feed;

		public OnCommentClick(FeedVO feed) {
			this.feed = feed;
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent((MainActivity) context, CommentActivity.class);
			// Bundle mBundle = new Bundle();
			// mBundle.putSerializable(Constant.IPET_PHOTO_SERIALIZABLE,
			// (Serializable) feed);
			// intent.putExtras(mBundle);
			((MainActivity) context).startActivity(intent);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}