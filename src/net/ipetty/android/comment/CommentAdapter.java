package net.ipetty.android.comment;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.WebLinkUtils;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.UserVO;

public class CommentAdapter extends BaseAdapter implements OnScrollListener {

    public final static String TAG = "CommentAdapter";
    private LayoutInflater inflater;
    private Context context;
    DisplayImageOptions options = AppUtils.getNormalImageOptions();
    private List<CommentVO> list = new ArrayList<CommentVO>(); // 这个就本地dataStore

    public CommentAdapter(Context context) {
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
        return list.get(position).getId();
    }

    // 构建一个布局缓存的结构体 与VO对应
    public class ViewHolder {

        public TextView text;
        public TextView timestamp;
        public ImageView avatar;
    }

    private ViewHolder holder;

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

        CommentVO commentVo = list.get(position);
        String creatAt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(commentVo.getCreatedOn());
        holder.timestamp.setText(creatAt);

        asynLoadUserInfo((Activity) context, holder, commentVo);
        return view;
    }

    //异步加载
    public void asynLoadUserInfo(Activity activity, final ViewHolder holder, final CommentVO commentVo) {
        int userId = commentVo.getCreatedBy();

        new GetUserById(activity)
                .setListener(new DefaultTaskListener<UserVO>(activity) {
                    @Override
                    public void onSuccess(UserVO result) {
                        String nickName = result.getNickname() == null ? "无名氏" : result.getNickname();
                        String username = "<b><a href='1'>[" + nickName + "]</a></b>";
                        String text = commentVo.getText();
                        String html = username + ":" + text;
                        WebLinkUtils.setUserLinkClickIntercept((Activity) context, holder.text, html);

                        if (null != result.getAvatar()) {
                            ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + result.getAvatar(), holder.avatar, options);
                        }

                    }
                })
                .execute(userId);

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
