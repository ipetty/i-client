package net.ipetty.android.feed;

import java.io.File;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.ImageUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedPublishActivity extends Activity {
	private static final String TAG = "FeedPublishActivity";
	private String path;
	private String outPath;
	private DisplayImageOptions options;
	private EditText edit;
	private TextView btn_publish;
	private Integer hasCompress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_publish);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_feed_publish));
		btnBack.setOnClickListener(new BackClickListener(this));

		options = AppUtils.getNormalImageOptions();

		// photoPath
		Intent intent = getIntent();
		path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		outPath = intent.getStringExtra(Constant.INTENT_PHOTO_OUT_PATH_KEY);
		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.fromFile(new File(path)).toString();
		ImageLoader.getInstance().displayImage(uri, image, options);

		edit = (EditText) this.findViewById(R.id.editText);
		btn_publish = (TextView) this.findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(onClickPublish);

		// 图片压缩
		new CompressTask().execute();
	}

	private final OnClickListener onClickPublish = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Editable text = edit.getText();
			String str = text.toString();

			// 发布操作
			new FeedPublishTask().execute();
		}

	};

	public class CompressTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			// 压缩图片
			int result = ImageUtils.compressImage(path, outPath);
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			hasCompress = result;
		}

	}

	// AsyncTask
	public class FeedPublishTask extends AsyncTask<Integer, Integer, Integer> {
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			String str = getResources().getString(R.string.publish_loading);
			this.progress = ProgressDialog.show(FeedPublishActivity.this, null, str);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			while (hasCompress == null) {
				try {
					// 等待图片压缩线程完成
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.progress.dismiss();
			return hasCompress;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

		}
	}
}
