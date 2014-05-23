package net.ipetty.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.domain.CommentVO;
import net.ipetty.android.domain.UserVO;
import net.ipetty.android.ui.adapter.FansAdapter;
import net.ipetty.android.ui.event.BackClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FansActivity extends BaseActivity {
	public final static String TAG = "FansActivity";
	private FansAdapter adapter; // 定义适配器
	private PullToRefreshListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fans);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_fans));
		btnBack.setOnClickListener(new BackClickListener(this));

		listView = (PullToRefreshListView) this.findViewById(R.id.listView);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new FansTask().execute();
			}
		});

		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				loadMoreData(getList(20));
			}
		});

		// 初始化适配器
		adapter = new FansAdapter(this);
		listView.setAdapter(adapter);
		loadData();

	}

	// TODO:这部分方法没考虑好 是放在adapter中 还是 Activity中
	// 加载数据
	public void loadData() {
		adapter.setList(this.getList(0));
		adapter.notifyDataSetChanged(); // 这个方法刷新界面，会重载所有的 getView
	}

	public void loadData(List<CommentVO> list) {
		adapter.setList(this.getList(0));
		adapter.notifyDataSetChanged(); // 这个方法刷新界面，会重载所有的 getView
	}

	// 加载更多数据
	public void loadMoreData(List<CommentVO> list) {
		adapter.getList().addAll(list);
		adapter.notifyDataSetChanged(); // 这个方法刷新界面，会重载所有的 getView
	}

	// 模拟数据
	public List<CommentVO> getList(int x) {
		List<CommentVO> list = new ArrayList<CommentVO>(0);
		for (int i = x; i < x + 20; i++) {
			CommentVO vo = new CommentVO();
			vo.setId(i);
			vo.setText("text" + i);

			UserVO u = new UserVO();
			u.setId(i);
			u.setName("user" + i);
			vo.setUser(u);

			list.add(vo);
		}
		return list;
	}

	private class FansTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			loadData(getList(0));
			listView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fans, menu);
		return true;
	}

}
