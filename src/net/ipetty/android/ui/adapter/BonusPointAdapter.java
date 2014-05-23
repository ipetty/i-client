package net.ipetty.android.ui.adapter;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.domain.CommentVO;
import net.ipetty.android.domain.UserVO;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BonusPointAdapter extends BaseAdapter implements OnScrollListener {
	public final static String TAG = "BonusPointAdapter";
	private LayoutInflater inflater;
	private List<CommentVO> list = null; // 这个就本地dataStore

	public BonusPointAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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
	public class ViewHolder {
		public TextView text;
		public TextView name;
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
			view = inflater.inflate(R.layout.list_bonus_point_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.text);
			holder.name = (TextView) view.findViewById(R.id.name);
			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 数据与界面绑定
		CommentVO vo = (CommentVO) this.getItem(position);
		UserVO u = vo.getUser();
		holder.text.setText(vo.getText());
		holder.name.setText(u.getName());
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

	public List<CommentVO> getList() {
		return list;
	}

	public void setList(List<CommentVO> list) {
		this.list = list;
	}

}
