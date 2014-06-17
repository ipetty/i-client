package net.ipetty.android.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.ImageUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.feed.FeedPublishActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.ListByTimelineForHomePage;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.vo.UserVO;

public class MainHomeFragment extends Fragment {

    public final static String TAG = MainHomeFragment.class.getSimpleName();

    private Activity activity;
    private LinkedList<String> mListItems;
    private PullToRefreshListView mPullRefreshListView;
    private FeedAdapter mAdapter;
    //private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats"};
    private Dialog cameraDialog;
    private ImageView avatar;
    private Integer pageNumber = 0;
    private final Integer pageSize = 5;

    private Dialog headBgDialog;
    private List<ModDialogItem> head_bg_items;

    DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.main_fragment_home, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        options = AppUtils.getNormalImageOptions();
        initListView();
        initCamera();

    }

    //获取刷新时间，若网络不可用则取最后一次刷新时间
    private Long getRefreshTime() {

        if (NetWorkUtils.isNetworkConnected(activity)) {
            return System.currentTimeMillis();
        }

        return MyAppStateManager.getLastRefrsh4Home(activity);
    }

    private void initListView() {
        // TODO Auto-generated method stub
        this.activity = getActivity();

        mPullRefreshListView = (PullToRefreshListView) this.activity.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(activity.getApplicationContext(), getRefreshTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL
                );

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                //下拉刷新数据
                new ListByTimelineForHomePage(MainHomeFragment.this.activity)
                        .setListener(new GetFeedListListener(MainHomeFragment.this.activity,
                                        mAdapter,
                                        mPullRefreshListView,
                                        true))
                        .execute(getRefreshTime().toString(), pageNumber.toString(), pageSize.toString());
                //重置页号
                pageNumber = 0;
            }
        });

        mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                //加载更多
                new ListByTimelineForHomePage(MainHomeFragment.this.activity)
                        .setListener(new GetFeedListListener(MainHomeFragment.this.activity,
                                        mAdapter,
                                        mPullRefreshListView,
                                        false))
                        .execute(getRefreshTime().toString(), pageNumber.toString(), pageSize.toString());
                pageNumber++;
            }
        });

        mListItems = new LinkedList<String>();

        ListView actualListView = mPullRefreshListView.getRefreshableView();

        initHeaderView(actualListView);

        mAdapter = new FeedAdapter(this.activity);
        actualListView.setAdapter(mAdapter);
        //初始加载数据
        new ListByTimelineForHomePage(this.activity)
                .setListener(new GetFeedListListener(this.activity, mAdapter))
                .execute(getRefreshTime().toString(), pageNumber.toString(), pageSize.toString());
    }

    private void initHeaderView(ListView listView) {
        //获取UserVO
        IpetApi api = IpetApi.init(activity);
        //此API特殊处理，可以在UI线程调用
        UserVO currUser = api.getUserApi().getById(api.getCurrUserId());

        head_bg_items = new ArrayList<ModDialogItem>();
        head_bg_items.add(new ModDialogItem(null, "更换相册封面", headBgOnClick));

        View v = activity.getLayoutInflater().inflate(R.layout.list_feed_header, listView, false);
        avatar = (ImageView) v.findViewById(R.id.avatar);
        //设置头像
        ImageLoader.getInstance().displayImage(currUser.getAvatar(), avatar, options);

        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity, SpaceActivity.class);
                startActivity(intent);
            }
        });

        ImageView header_bg = (ImageView) v.findViewById(R.id.header_bg);
        header_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                headBgDialog = DialogUtils.modPopupDialog(activity, head_bg_items, headBgDialog);
            }
        });

        // 根据个人信息修改背景
        ImageLoader.getInstance().displayImage(currUser.getBackground(), header_bg, options);
        listView.addHeaderView(v);

    }

    private void initCamera() {
        ImageView camera = (ImageView) activity.findViewById(R.id.composer_buttons_show_hide_button);
        camera.setOnClickListener(cameraClick);
    }

    private OnClickListener cameraClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            OnClickListener[] Listener = new OnClickListener[]{takePhotoClick, pickPhotoClick};
            cameraDialog = DialogUtils.bottomPopupDialog(activity, Listener, R.array.alert_camera, getString(R.string.camera_title), cameraDialog);
        }
    };

    private final OnClickListener takePhotoClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DeviceUtils.takePicture(MainHomeFragment.this);
            cameraDialog.cancel();
        }
    };

    private final OnClickListener pickPhotoClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DeviceUtils.chooserSysPics(MainHomeFragment.this);
            cameraDialog.cancel();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, requestCode + " " + resultCode);
        if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                Uri uri = data.getData();
                String path = PathUtils.getPathByUriFromFile(uri, activity);
                compressImage(path);
            }
        }

        if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
            if (resultCode == FragmentActivity.RESULT_OK) {
                String path = MyAppStateManager.getCameraTempFile(activity);
                Log.i(TAG, "-->file path:" + path);
                compressImage(path);

            }
        }
    }

    private void compressImage(String path) {
        if (!ImageUtils.isCorrectSize(path)) {
            Toast.makeText(activity, "您选择的图片过小，请大于" + Constant.COMPRESS_IMAGE_MIN_WIDTH + "x" + Constant.COMPRESS_IMAGE_MIN_HEIGHT, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(activity, FeedPublishActivity.class);
        Bundle bundle = new Bundle();// 该类用作携带数据
        bundle.putString(Constant.INTENT_PHOTO_PATH_KEY, path);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private OnClickListener headBgOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO:切换到背景图修改界面
            Toast.makeText(activity, "暂未实现", Toast.LENGTH_SHORT).show();
            headBgDialog.cancel();
        }
    };

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}
