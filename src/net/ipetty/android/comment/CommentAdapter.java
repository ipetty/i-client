package net.ipetty.android.comment;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.demo.CommentVO;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter implements OnScrollListener {
	public final static String TAG = "CommentAdapter";
	private LayoutInflater inflater;
	private Context context;
	private List<CommentVO> list = null; // 这个就本地dataStore

	public CommentAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return 2;// list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return 0;// list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getId();
	}

	// 构建一个布局缓存的结构体 与VO对应
	public class ViewHolder {
		public TextView text;
		public TextView timestamp;
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
			view = inflater.inflate(R.layout.list_comment_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.text);
			holder.timestamp = (TextView) view.findViewById(R.id.timestamp);
			convertView = view;
			convertView.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		String username = "<b><a href='1'>小王</a></b>";
		String text = "很好很不错";
		String html = username + ":" + text;
		WebLinkUtils.setUserLinkClickIntercept((Activity) context, holder.text, html);
		// 数据与界面绑定
		// CommentVO vo = (CommentVO) this.getItem(position);
		// UserVO u = vo.getUser();
		// holder.text.setText(vo.getText());
		// holder.name.setText(u.getName());
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
