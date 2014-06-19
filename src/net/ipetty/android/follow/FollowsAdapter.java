package net.ipetty.android.follow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import net.ipetty.R;
import net.ipetty.vo.CommentVO;

public class FollowsAdapter extends BaseAdapter implements OnScrollListener {

    public final static String TAG = "FollowsAdapter";
    private LayoutInflater inflater;
    private List<CommentVO> list = null; // 这个就本地dataStore

    public FollowsAdapter(Context context) {
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
        return 0;// list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;// list.get(position).getId();
    }

	// 构建一个布局缓存的结构体 与VO对应
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
        Log.i(TAG, "list position-->" + position);
        // 这里开始呈现每个item的布局
        View view;
        if (convertView == null) {
            Log.i(TAG, "init items View");
            view = inflater.inflate(R.layout.list_follows_item, null);
            holder = new ViewHolder();
            holder.avatar = (ImageView) view.findViewById(R.id.avatar);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.follow = (ImageView) view.findViewById(R.id.follow);
            holder.follow.setOnClickListener(followClick);

            convertView = view;
            convertView.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        // 数据与界面绑定

        return view;
    }

    private OnClickListener followClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
			// TODO Auto-generated method stub
            // test
            ((ImageView) v).setImageResource(R.drawable.following_avatar);
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

    public List<CommentVO> getList() {
        return list;
    }

    public void setList(List<CommentVO> list) {
        this.list = list;
    }

}
