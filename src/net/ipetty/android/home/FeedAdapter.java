package net.ipetty.android.home;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.comment.CommentActivity;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.like.LikeActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

public class FeedAdapter extends BaseAdapter implements OnScrollListener {

    public final static String TAG = FeedAdapter.class.getSimpleName();
    private DisplayImageOptions options;
    private LayoutInflater inflater;
    private Context context;
    public List<ModDialogItem> more_items;
    private List<FeedVO> list = new ArrayList<FeedVO>(0); // 这个就本地dataStore
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
        Log.d(TAG, "getCount" + list.size());
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "getItem:" + position);
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        FeedVO feed = list.get(position);
        Log.d(TAG, "getItemId" + feed.getId());
        return feed.getId();
    }

    // 构建一个布局缓存的结构体 与VO对应
    public class ViewHolder {

        public ImageView avatar;
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
        public TextView comments_num;
        public LinearLayout comments_group_list;

    }

    public ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "list position-->" + position);
        // 这里开始呈现每个item的布局
        View view;
        if (convertView == null) {
            Log.i(TAG, "init items View");
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
            holder.avatar = (ImageView) view.findViewById(R.id.avatar);
            holder.avatar.setOnClickListener(userInfoOnClick);
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

            // 评论
            holder.comments_num = (TextView) view.findViewById(R.id.row_feed_photo_textview_comments_num);
            holder.comments_group_list = (LinearLayout) view.findViewById(R.id.row_feed_photo_comments_list);

            convertView = view;
            convertView.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        FeedVO feed = list.get(position);
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

    private void initDefaultView(ViewHolder holder, final FeedVO feed, int position) {
        Log.d(TAG, "--initDefaultView--");
        Integer uid = feed.getCreatedBy();
        UserVO user = IpetApi.init(context).getUserApi().getById(uid);

        // 发布人头像
        Log.d(TAG, "发布人头像：" + user.getAvatar());
        if (StringUtils.isEmpty(user.getAvatar())) {
            ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), holder.avatar, options);
        }
        Log.d(TAG, "发布昵称：" + user.getNickname());
        holder.nickname.setText(user.getNickname());
        String creatAt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(feed.getCreatedOn());
        holder.created_at.setText(creatAt);
        holder.content.setText(feed.getText());
        Log.d(TAG, "发布图片small：" + feed.getImageSmallURL());
        Log.d(TAG, "发布图片Original：" + feed.getImageOriginalURL());
        ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + feed.getImageSmallURL(), holder.content_image, options);
        //内容空不显示内容区域
        if (StringUtils.isEmpty(feed.getText())) {
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
                Intent intent = new Intent((MainActivity) context, LargerImageActivity.class);
                intent.putExtra("url", Constant.FILE_SERVER_BASE + feed.getImageOriginalURL());
                ((MainActivity) context).startActivity(intent);
                // Toast.makeText(context, "暂未实现", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnClickListener userInfoOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(context, SpaceActivity.class);
            context.startActivity(intent);
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

        // 赞
        String html = "<b><a href='1'>张三</a>,<a href='2'>李四四</a>,<a href='3'>王五</a></b>";
        String liked_num_text = this.context.getResources().getString(R.string.liked_num_text);
        String likedNum = "10";
        html += String.format(liked_num_text, likedNum);
        WebLinkUtils.setUserLinkIntercept((Activity) context, holder.like_text, html);

        // 评论 总数
        holder.comments_num.setOnClickListener(myCommentClick);
        String commentNumStr = this.context.getResources().getString(R.string.comment_num_text);
        String commentsNum = String.valueOf(feed.getCommentCount());
        holder.comments_num.setText(String.format(commentNumStr, commentsNum));

        // 动态添加评论
        // TODO: for 循环
        holder.comments_group_list.addView(addComment());
        holder.comments_group_list.addView(addComment());
    }

    private RelativeLayout addComment() {
        String username = "<b><a href='1'>小王</a></b>";
        String text = "很好很不错";

        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.list_feed_item_feedback_comment, null);
        TextView t = (TextView) layout.findViewById(R.id.row_feed_textview_comments_item);
        String html = username + " : " + text;
        WebLinkUtils.setUserLinkClickIntercept((Activity) context, t, html);

        return layout;
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
