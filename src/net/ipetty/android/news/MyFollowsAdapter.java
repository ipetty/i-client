package net.ipetty.android.news;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

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

public class MyFollowsAdapter extends BaseAdapter implements OnScrollListener {

	private String TAG = getClass().getSimpleName();

	private LayoutInflater inflater;
	private List<UserVO> list = new ArrayList<UserVO>(); // 这个就本地dataStore
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	public MyFollowsAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
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

		public ImageView avatar;
		public TextView name;
		public ImageView follow;
	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "list position-->" + position);
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			Log.d(TAG, "init items View");
			view = inflater.inflate(R.layout.list_my_follows_item, null);
			holder = new ViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.avatar);
			holder.name = (TextView) view.findViewById(R.id.name);

			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 数据与界面绑定
		UserVO user = list.get(position);
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance()
					.displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), holder.avatar, options);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}
		holder.name.setText(user.getNickname());
		// TODO:对于关注、反关注图标和点击事件处理

		return view;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	public List<UserVO> getList() {
		return list;
	}

	public void setList(List<UserVO> list) {
		this.list = list;
	}

}
