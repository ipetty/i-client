package net.ipetty.android.like;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.vo.FeedFavorVO;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LikeAdapter extends BaseAdapter implements OnScrollListener {

	public final static String TAG = LikeAdapter.class.getSimpleName();
	private Context context;
	DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private LayoutInflater inflater;
	private List<FeedFavorVO> list = new ArrayList<FeedFavorVO>(); // 这个就本地dataStore

	public LikeAdapter(Context context) {
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
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getCreatedBy();
	}

	// 构建一个布局缓存的结构体 与VO对应
	private class ViewHolder {

		public TextView name;
		public ImageView avatar;
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "list position-->" + position);
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			Log.i(TAG, "init items View");
			view = inflater.inflate(R.layout.list_like_item, null);
			holder = new ViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.name = (TextView) view.findViewById(R.id.name);
			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		FeedFavorVO vo = list.get(position);
		// asynLoadUserInfo((Activity) context, holder, vo);
		UserVO user = UserApiWithCache.getUserById4Synchronous(context, vo.getCreatedBy());
		holder.name.setText(user.getNickname());
		ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), holder.avatar, options);
		return view;
	}

	// 异步加载
	public void asynLoadUserInfo(Activity activity, final ViewHolder holder, final FeedFavorVO vo) {
		int userId = vo.getCreatedBy();

		new GetUserById(activity).setListener(new DefaultTaskListener<UserVO>(activity) {
			@Override
			public void onSuccess(UserVO result) {
				if (null != result.getNickname()) {
					holder.name.setText(result.getNickname());
				}
				if (null != result.getAvatar()) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + result.getAvatar(),
							holder.avatar, options);
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

	public List<FeedFavorVO> getList() {
		return list;
	}

	public void setList(List<FeedFavorVO> list) {
		this.list = list;
	}

}
