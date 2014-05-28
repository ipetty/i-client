package net.ipetty.android.ui.adapter;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.ui.CommentActivity;
import net.ipetty.android.ui.LikeActivity;
import net.ipetty.android.ui.MainActivity;
import net.ipetty.vo.FeedVO;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FeedAdapter extends BaseAdapter implements OnScrollListener {

    public final static String TAG = "ListFeedAdapter";
    private LayoutInflater inflater;
    private Context context;
    private List list = null; // 这个就本地dataStore

    public FeedAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
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

        public ImageView btn_liked;
        public View liked_detail;
        public ImageView btn_comment;
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

            holder.btn_liked = (ImageView) view.findViewById(R.id.feed_button_like);
            holder.btn_comment = (ImageView) view.findViewById(R.id.feed_button_comment);
            holder.liked_detail = view.findViewById(R.id.row_feed_photo_textview_likes);

            convertView = view;
            convertView.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        FeedVO feed = new FeedVO();

        initLikedBtnView(holder, feed, position);
        initCommentView(holder, feed, position);
        // 数据与界面绑定

        return view;
    }

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
                Intent intent = new Intent((MainActivity) context, LikeActivity.class);
                ((MainActivity) context).startActivity(intent);
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
