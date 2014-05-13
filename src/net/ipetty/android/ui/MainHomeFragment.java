package net.ipetty.android.ui;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import net.ipetty.R;
import net.ipetty.android.ui.adapter.ListFeedAdapter;
import net.ipetty.android.utils.DeviceUtils;
import net.ipetty.android.utils.DialogUtils;
import net.ipetty.android.utils.ImageUtils;
import net.ipetty.android.utils.PathUtils;
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

public class MainHomeFragment extends Fragment {
	public final static String TAG = "MainHomeFragment";
	private Activity activity;
	private LinkedList<String> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private ListFeedAdapter mAdapter;
	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats" };
	private Dialog cameraDialog;
	private String mImageName;
	private ImageView avator;

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
			OnClickListener[] Listener = new OnClickListener[] { takePhotoClick, pickPhotoClick };
			cameraDialog = DialogUtils.bottomPopupDialog(activity, Listener, R.array.alert_camera, getString(R.string.camera_title), cameraDialog);
		}
	};

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mImageName = (System.currentTimeMillis() + ".jpg");
			DeviceUtils.takePicture(MainHomeFragment.this, PathUtils.getCarmerDir(), mImageName);
			cameraDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mImageName = (System.currentTimeMillis() + ".jpg");
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
				File file = new File(PathUtils.getCarmerDir(), mImageName);
				String path = file.getPath();
				compressImage(path);

			}
		}
	}

	private void compressImage(String path) {
		String outPath = new File(PathUtils.getCarmerDir(), mImageName).getPath();
		int result = ImageUtils.compressImage(path, outPath);
		if (result == ImageUtils.RESULT_FAILTRUE) {
			Toast.makeText(activity, "您选择的图片过小，请大于64x64", Toast.LENGTH_LONG).show();
			return;
		}
		if (result == ImageUtils.RESULT_ERROR) {
			Toast.makeText(activity, "操作失败", Toast.LENGTH_LONG).show();
			return;
		}

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

		mAdapter = new ListFeedAdapter(this.activity);
		actualListView.setAdapter(mAdapter);
	}

	private void initHeaderView(ListView listView) {
		View v = activity.getLayoutInflater().inflate(R.layout.list_feed_header, listView, false);
		avator = (ImageView) v.findViewById(R.id.avator);
		listView.addHeaderView(v);
	}

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
