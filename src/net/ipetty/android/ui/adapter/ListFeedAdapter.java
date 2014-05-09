package net.ipetty.android.ui.adapter;

import java.util.List;

import net.ipetty.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

public class ListFeedAdapter extends BaseAdapter implements OnScrollListener {
	public final static String TAG = "ListFeedAdapter";
	private LayoutInflater inflater;
	private List list = null; // 这个就本地dataStore

	public ListFeedAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
	}

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

	}

	public ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 这里开始呈现每个item的布局
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_feed_item, null);
			holder = new ViewHolder();

			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		// 数据与界面绑定

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

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

}
