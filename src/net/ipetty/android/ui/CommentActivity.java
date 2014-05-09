package net.ipetty.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.domain.CommentVO;
import net.ipetty.android.domain.UserVO;
import net.ipetty.android.ui.adapter.CommentAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CommentActivity extends BaseActivity {
	public final static String TAG = "CommentActivity";
	private CommentAdapter adapter; // 定义适配器
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Log.i(TAG, "onCreate");

		listView = (ListView) this.findViewById(R.id.listView);
		// 初始化适配器
		adapter = new CommentAdapter(this);

		listView.setAdapter(adapter);

		// TODO:还不确定在哪个个生命周期执行第一次数据加载 特别是内嵌多个fragment的时候
		// 加载数据 - 这里省略了异步加载数据
		loadData(this.getList(0));

		// 加载更多按钮
		Button btn = (Button) this.findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (adapter.getCount() > 20) {
					Toast.makeText(CommentActivity.this, "已经加载过了", Toast.LENGTH_SHORT).show();
					return;
				}
				loadMoreData(getList(20));
			}
		});
	}

	// TODO:这部分方法没考虑好 是放在adapter中 还是 Activity中
	// 加载数据
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

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
}
