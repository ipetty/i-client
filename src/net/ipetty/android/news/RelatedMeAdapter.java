package net.ipetty.android.news;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.vo.ActivityVO;
import net.ipetty.vo.UserVO;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RelatedMeAdapter extends BaseAdapter implements OnScrollListener {

	public final static String TAG = RelatedMeAdapter.class.getSimpleName();
	public Context context;
	private LayoutInflater inflater;
	private List<ActivityVO> list = new ArrayList<ActivityVO>(); // 这个就本地dataStore

	public RelatedMeAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getId();
	}

	// 构建一个布局缓存的结构体 与VO对应
	public class NewView {
		public View favorView;
	}

	public class ViewHolder {
		public String type;
		public ImageView avatar; // 称赞人or关注人头像or回复人
		public TextView createdBy; // 称赞人or关注人名称or回复人
		public TextView createdOn; // 称赞时间or关注时间or回复时间
		public TextView content;// 回复内容
		public ImageView relatedImage; // 称赞的图片or回复帖子的图片
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "list position-->" + position);
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.list_related_me_item, null);
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.createdOn = (TextView) view.findViewById(R.id.createdOn);
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.relatedImage = (ImageView) view.findViewById(R.id.relatedImage);

			convertView = view;
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();// 取出ViewHolder对象
		}

		ActivityVO act = list.get(position);
		if (Constant.NEWS_TYPE_FAVOR.equals(act.getType())) {
			initFavorView(holder, act);
		} else if (Constant.NEWS_TYPE_FOLLOWED.equals(act.getType())) {
			initFollowedView(holder, act);
		} else if (Constant.NEWS_TYPE_COMMENT.equals(act.getType())) {
			initCommentView(holder, act);
		}

		return view;
	}

	public void initFavorView(ViewHolder holder, ActivityVO act) {
		String str = context.getResources().getString(R.string.news_item_favor);
		initContent(holder.content, act.getCreatedBy(), str);
		holder.relatedImage.setVisibility(View.VISIBLE);
	}

	public void initFollowedView(ViewHolder holder, ActivityVO act) {
		String str = context.getResources().getString(R.string.news_item_follow);
		initContent(holder.content, act.getCreatedBy(), str);
		holder.relatedImage.setVisibility(View.INVISIBLE);
	}

	public void initCommentView(ViewHolder holder, ActivityVO act) {
		String str = context.getResources().getString(R.string.news_item_comment);
		str += ": " + "是不又有变胖";
		initContent(holder.content, act.getCreatedBy(), str);
		holder.relatedImage.setVisibility(View.VISIBLE);
	}

	public void initContent(TextView tx, Integer id, String text) {
		String nickname = "张三";// this.getCacheUserById(id).getNickname();
		String html = "<b><a href='" + 0 + "'>" + nickname + "</a></b>";
		html = html + " " + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) context, tx, html);
	}

	public UserVO getCacheUserById(int id) {
		return UserApiWithCache.getUserById4Synchronous(context, id);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public List<ActivityVO> getList() {
		return list;
	}

	public void setList(List<ActivityVO> list) {
		this.list = list;
	}

}
