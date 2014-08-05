package net.ipetty.android.home;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.MyAppStateManager;
import net.ipetty.android.core.ui.BaseFragment;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.ImageUtils;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.feed.FeedPublishActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.feed.ListByTimelineForHomePage;
import net.ipetty.android.space.SpaceActivity;
import net.ipetty.android.update.UpdateManager;
import net.ipetty.vo.FeedVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
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

public class MainHomeFragment extends BaseFragment {

	public final static String TAG = MainHomeFragment.class.getSimpleName();

	private PullToRefreshListView mPullRefreshListView;
	private FeedAdapter mAdapter;
	private Dialog cameraDialog;
	private ImageView avatar;
	private Integer pageNumber = 0;
	private final Integer pageSize = 20;
	private Long lastTimeMillis;

	private Dialog headBgDialog;
	private List<ModDialogItem> head_bg_items;

	// 背景图片
	private ImageView header_bg;

	DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private Boolean hasMore = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastTimeMillis = MyAppStateManager.getLastRefrsh4Home(this.getActivity());
		Log.d(TAG, "onCreate");

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROADCAST_INTENT_FEED_COMMENT);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_FAVORED);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_PUBLISH);
		filter.addAction(Constant.BROADCAST_INTENT_FEED_DELETE);
		filter.addAction(Constant.BROADCAST_INTENT_CCOMMENT_DELETE);
		this.getActivity().registerReceiver(broadcastreciver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		return inflater.inflate(R.layout.main_fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		initListView();
		initCamera();
		loadData();
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		super.onViewReady(savedInstanceState);

		// 检查软件更新
		UpdateManager manager = new UpdateManager(this.getActivity());
		manager.checkUpdate();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
		super.onViewResume();
		refreshData();
	}

	public void loadMoreForResult(List<FeedVO> result) {
		Log.d(TAG, "loadMoreForResult:" + result.size());
		if (result.size() < pageSize) {
			hasMore = false;
		}
		if (result.size() > 0) {
			mAdapter.getList().addAll(result);
			mAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}
	}

	// 获取刷新时间，若网络不可用则取最后一次刷新时间
	private Long getRefreshTime() {
		if (NetWorkUtils.isNetworkConnected(this.getActivity())) {
			this.lastTimeMillis = System.currentTimeMillis();
			MyAppStateManager.setLastRefrsh4Home(this.getActivity(), this.lastTimeMillis);
			return this.lastTimeMillis;
		}

		return MyAppStateManager.getLastRefrsh4Home(this.getActivity());
	}

	private void initListView() {

		mPullRefreshListView = (PullToRefreshListView) this.getActivity().findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(MainHomeFragment.this.getActivity().getApplicationContext(),
						getRefreshTime(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 刷新数据
				new ListByTimelineForHomePage(MainHomeFragment.this.getActivity()).setListener(
						new PullToRefreshFeedListListener(MainHomeFragment.this.getActivity(), mAdapter,
								mPullRefreshListView)).execute(getRefreshTime().toString(), "0", pageSize.toString());
				// 重置页号
				pageNumber = 0;
				hasMore = true;
			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// 加载更多
				if (hasMore) {
					new ListByTimelineForHomePage(MainHomeFragment.this.getActivity()).setListener(
							new LoadMoreFeedListListener(MainHomeFragment.this)).execute(
							MainHomeFragment.this.lastTimeMillis.toString(), (++pageNumber).toString(),
							pageSize.toString());

				}
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// initHeaderView(actualListView);
		mAdapter = new FeedAdapter(this.getActivity());
		actualListView.setAdapter(mAdapter);

	}

	private void refreshData() {
		// 获取UserVO
		IpetApi api = IpetApi.init(this.getActivity());
		mAdapter.notifyDataSetChanged();
		/*
		 * UserApiWithCache.getUserById4Asynchronous(this.getActivity(),
		 * api.getCurrUserId(), new
		 * DefaultTaskListener<UserVO>(this.getActivity()) {
		 * 
		 * @Override public void onSuccess(UserVO result) { // 设置头像 if
		 * (StringUtils.isNotEmpty(result.getAvatar())) {
		 * ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE +
		 * result.getAvatar(), avatar, options); } else {
		 * avatar.setImageResource(R.drawable.avatar); } // 根据个人信息加载背景 if
		 * (StringUtils.isNotEmpty(result.getBackground())) {
		 * ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE +
		 * result.getBackground(), header_bg, options); }
		 * 
		 * mAdapter.notifyDataSetChanged(); } });
		 */
	}

	private void loadData() {
		// 获取UserVO
		IpetApi api = IpetApi.init(this.getActivity());
		/*
		 * UserApiWithCache.getUserById4Asynchronous(this.getActivity(),
		 * api.getCurrUserId(), new
		 * DefaultTaskListener<UserVO>(this.getActivity()) {
		 * 
		 * @Override public void onSuccess(UserVO result) { // 设置头像 if
		 * (StringUtils.isNotEmpty(result.getAvatar())) {
		 * ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE +
		 * result.getAvatar(), avatar, options); } else {
		 * avatar.setImageResource(R.drawable.avatar); } new
		 * ListPetsByUserId(getActivity()).setListener( new
		 * DefaultTaskListener<List<PetVO>>(getActivity()) {
		 * 
		 * @Override public void onSuccess(List<PetVO> pets) { PetVO pet =
		 * pets.get(0); if (StringUtils.isNotBlank(pet.getFamily())) {
		 * header_bg.
		 * setImageResource(Constant.PET_FAMILY_RES_MAP.get(pet.getFamily())); }
		 * } }).execute(result.getId());
		 * 
		 * if (StringUtils.isNotEmpty(result.getBackground())) {
		 * ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE +
		 * result.getBackground(), header_bg, options); }
		 * 
		 * new ListByTimelineForHomePage(MainHomeFragment.this.getActivity()).
		 * setListener( new
		 * InitFeedListListener(MainHomeFragment.this.getActivity(),
		 * mAdapter)).execute( getRefreshTime().toString(), "0",
		 * pageSize.toString()); } });
		 */

		new ListByTimelineForHomePage(MainHomeFragment.this.getActivity()).setListener(
				new InitFeedListListener(MainHomeFragment.this.getActivity(), mAdapter)).execute(
				getRefreshTime().toString(), "0", pageSize.toString());

	}

	private void initHeaderView(ListView listView) {

		head_bg_items = new ArrayList<ModDialogItem>();
		// head_bg_items.add(new ModDialogItem(null, "更换相册封面", headBgOnClick));

		View v = this.getActivity().getLayoutInflater().inflate(R.layout.list_feed_header, listView, false);
		avatar = (ImageView) v.findViewById(R.id.avatar);

		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainHomeFragment.this.getActivity(), SpaceActivity.class);
				startActivity(intent);
			}
		});

		header_bg = (ImageView) v.findViewById(R.id.header_bg);
		header_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 修改背景图片
				headBgDialog = DialogUtils.modPopupDialog(MainHomeFragment.this.getActivity(), head_bg_items,
						headBgDialog);
				headBgDialog.cancel();
			}
		});

		int id = IpetApi.init(this.getActivity()).getCurrUserId();
		UserVO user = UserApiWithCache.getUserById4Synchronous(getActivity(), id);
		if (StringUtils.isNotBlank(user.getAvatar())) {
			ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
		} else {
			avatar.setImageResource(R.drawable.avatar);
		}

		listView.addHeaderView(v);
	}

	private void initCamera() {
		ImageView camera = (ImageView) this.getActivity().findViewById(R.id.composer_buttons_show_hide_button);
		camera.setOnClickListener(cameraClick);
	}

	private OnClickListener cameraClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			OnClickListener[] Listener = new OnClickListener[] { takePhotoClick, pickPhotoClick };
			cameraDialog = DialogUtils.bottomPopupDialog(MainHomeFragment.this.getActivity(), Listener,
					R.array.alert_camera, getString(R.string.camera_title), cameraDialog);
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
		Log.d(TAG, requestCode + " " + resultCode);
		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				String path = PathUtils.getPathByUriFromFile(uri, this.getActivity());
				compressImage(path);
			}
		}

		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				String path = MyAppStateManager.getCameraTempFile(this.getActivity());
				compressImage(path);

			}
		}
	}

	private void compressImage(String path) {
		if (!ImageUtils.isCorrectSize(path)) {
			Toast.makeText(this.getActivity(),
					"您选择的图片过小，请大于" + Constant.COMPRESS_IMAGE_MIN_WIDTH + "x" + Constant.COMPRESS_IMAGE_MIN_HEIGHT,
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(this.getActivity(), FeedPublishActivity.class);
		Bundle bundle = new Bundle();// 该类用作携带数据
		Log.d(TAG, "-->file path:" + path);
		bundle.putString(Constant.INTENT_PHOTO_PATH_KEY, path);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	// 更换相册封面
	private OnClickListener headBgOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO:切换到背景图修改界面
			Toast.makeText(MainHomeFragment.this.getActivity(), "暂未实现", Toast.LENGTH_SHORT).show();
			headBgDialog.cancel();
		}
	};

	private void updateFeedVO(FeedVO feedVO) {
		// TODO Auto-generated method stub
		this.mAdapter.updateFeedVO(feedVO);
	}

	protected void updateFavor(FeedVO feedVO) {
		// TODO Auto-generated method stub
		this.mAdapter.updateFavor(feedVO);
	}

	protected void prependFeedVO(FeedVO feedVO) {
		// TODO Auto-generated method stub
		this.mAdapter.prependFeedVO(feedVO);
		this.mPullRefreshListView.getRefreshableView().setSelection(1);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "onStop");

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		this.getActivity().unregisterReceiver(broadcastreciver);
	}

	// broadcastreciver
	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constant.BROADCAST_INTENT_FEED_COMMENT.equals(action)) {
				String jsonStr = intent.getStringExtra(Constant.FEEDVO_JSON_SERIALIZABLE);
				FeedVO feedVO = JSONUtils.fromJSON(jsonStr, FeedVO.class);
				MainHomeFragment.this.updateFeedVO(feedVO);
			}

			if (Constant.BROADCAST_INTENT_FEED_FAVORED.equals(action)) {
				String jsonStr = intent.getStringExtra(Constant.FEEDVO_JSON_SERIALIZABLE);
				FeedVO feedVO = JSONUtils.fromJSON(jsonStr, FeedVO.class);
				MainHomeFragment.this.updateFavor(feedVO);
			}

			if (Constant.BROADCAST_INTENT_FEED_PUBLISH.equals(action)) {
				Log.d(TAG, "receive publish");
				String jsonStr = intent.getStringExtra(Constant.FEEDVO_JSON_SERIALIZABLE);
				FeedVO feedVO = JSONUtils.fromJSON(jsonStr, FeedVO.class);
				MainHomeFragment.this.prependFeedVO(feedVO);
			}

			if (Constant.BROADCAST_INTENT_FEED_DELETE.equals(action)) {
				Log.d(TAG, "receive delete");
				long feedId = intent.getLongExtra(Constant.FEEDVO_ID, -1l);
				if (feedId != -1l) {
					MainHomeFragment.this.mAdapter.removeById(feedId);
					MainHomeFragment.this.mAdapter.notifyDataSetChanged();
				}
			}

			if (Constant.BROADCAST_INTENT_CCOMMENT_DELETE.equals(action)) {
				Log.d(TAG, "receive delete comment");
				long commentId = intent.getLongExtra(Constant.CCOMMENT_ID, -1l);
				if (commentId != -1l) {
					MainHomeFragment.this.mAdapter.removeCommentById(commentId);
				}
			}
		}

	};

}
