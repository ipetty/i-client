package net.ipetty.android.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.Arrays;
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
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.feed.FeedPublishActivity;
import net.ipetty.android.space.SpaceActivity;

public class MainHomeFragment extends Fragment {

    public final static String TAG = "MainHomeFragment";

    private Activity activity;
    private LinkedList<String> mListItems;
    private PullToRefreshListView mPullRefreshListView;
    private FeedAdapter mAdapter;
    private String[] mStrings = {"Abbaye de Belloc", "Abbaye du Mont des Cats"};
    private Dialog cameraDialog;
    private ImageView avatar;

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

    private void initCamera() {
        // TODO Auto-generated method stub
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

    private void initListView() {
        // TODO Auto-generated method stub
        this.activity = getActivity();

        mPullRefreshListView = (PullToRefreshListView) this.activity.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(activity.getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new MainHomeTask().execute();
            }
        });

        mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                // TODO Auto-generated method stub

            }
        });

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        ListView actualListView = mPullRefreshListView.getRefreshableView();

        initHeaderView(actualListView);

        mAdapter = new FeedAdapter(this.activity);
        actualListView.setAdapter(mAdapter);
    }

    private void initHeaderView(ListView listView) {
        head_bg_items = new ArrayList<ModDialogItem>();
        head_bg_items.add(new ModDialogItem(null, "更换相册封面", headBgOnClick));

        View v = activity.getLayoutInflater().inflate(R.layout.list_feed_header, listView, false);
        avatar = (ImageView) v.findViewById(R.id.avatar);
        avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(activity, SpaceActivity.class);
                startActivity(intent);
            }
        });

        // TODO:根据个人信息修改头像
        // ImageLoader.getInstance().displayImage(url, avator, options);
        ImageView header_bg = (ImageView) v.findViewById(R.id.header_bg);

        header_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                headBgDialog = DialogUtils.modPopupDialog(activity, head_bg_items, headBgDialog);
            }
        });

        // TODO:根据个人信息修改背景
        // ImageLoader.getInstance().displayImage(url, header_bg, options);
        listView.addHeaderView(v);

    }

    private OnClickListener headBgOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO:切换到背景图修改界面
            Toast.makeText(activity, "暂未实现", Toast.LENGTH_SHORT).show();
            headBgDialog.cancel();
        }
    };

    private class MainHomeTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

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
