package net.ipetty.android.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.sdk.task.feed.GetFeedById;
import net.ipetty.android.sdk.task.feed.PublishComment;
import net.ipetty.vo.CommentVO;
import net.ipetty.vo.FeedVO;
import org.apache.commons.lang3.StringUtils;

public class CommentActivity extends BaseActivity {

	public final static String TAG = CommentActivity.class.getSimpleName();
	private CommentAdapter adapter; // 定义适配器
	private PullToRefreshListView listView;
	private EditText pulishTextView;
	private Long feedId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Log.d(TAG, "onCreate");
	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		feedId = this.getIntent().getExtras().getLong("feedId");

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_comment));
		btnBack.setOnClickListener(new BackClickListener(this));

		listView = (PullToRefreshListView) this.findViewById(R.id.listView);
		pulishTextView = (EditText) this.findViewById(R.id.editText);

		// 下拉刷新
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				new GetFeedById(CommentActivity.this).setListener(new DefaultTaskListener<FeedVO>(CommentActivity.this, "刷新中") {
					@Override
					public void onSuccess(FeedVO result) {
						adapter.setList(result.getComments());
						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();

					}
				}).execute(feedId);
			}
		});

		// 最后一条记录
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {

			}
		});

		// 初始化适配器
		adapter = new CommentAdapter(this);
		listView.setAdapter(adapter);

		// 发布评论
		View button = this.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = pulishTextView.getText().toString();

				if (!StringUtils.isEmpty(str)) {
					CommentVO vo = new CommentVO();
					vo.setFeedId(feedId);
					vo.setText(str);
					new PublishComment(CommentActivity.this).setListener(new DefaultTaskListener<FeedVO>(CommentActivity.this, "正在发布") {
						@Override
						public void onSuccess(FeedVO result) {
							adapter.setList(result.getComments());
							adapter.notifyDataSetChanged();
							pulishTextView.setText("");
							CommentActivity.this.reloadData(result);

							Intent intent = new Intent(Constant.BROADCAST_INTENT_FEED_COMMENT);
							Bundle mBundle = new Bundle();
							mBundle.putString(Constant.FEEDVO_JSON_SERIALIZABLE, JSONUtils.toJson(result));
							intent.putExtras(mBundle);
							CommentActivity.this.sendBroadcast(intent);
						}
					}).execute(vo);
				}

			}
		});
		loadData();
	}

	public void loadData() {

		new GetFeedById(this).setListener(new DefaultTaskListener<FeedVO>(this, "加载中") {
			@Override
			public void onSuccess(FeedVO result) {
				adapter.setList(result.getComments());
				adapter.notifyDataSetChanged();
			}
		}).execute(feedId);

	}

	public void reloadData(FeedVO feed) {
		adapter.setList(feed.getComments());
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}
}
