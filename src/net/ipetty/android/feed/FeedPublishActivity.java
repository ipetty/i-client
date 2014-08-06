package net.ipetty.android.feed;

import java.io.File;
import java.io.IOException;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.vo.FeedFormVO;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedPublishActivity extends BaseActivity {

	private String path;
	private DisplayImageOptions options;
	private EditText edit;
	private TextView btn_publish;
	private View location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_publish);

	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_feed_publish));
		btnBack.setOnClickListener(new BackClickListener(this));

		options = AppUtils.getNormalImageOptions();

		// photoPath
		Intent intent = getIntent();
		path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.decode(Uri.fromFile(new File(path)).toString());
		ImageLoader.getInstance().displayImage(uri, image, options);

		edit = (EditText) this.findViewById(R.id.editText);
		btn_publish = (TextView) this.findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(onClickPublish);

		// location
		location = this.findViewById(R.id.location_layout);
		// 选择位置
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// FeedPublishActivity.this.showMessageForShortTime("位置功能暂未实现");
				Intent intent = new Intent(FeedPublishActivity.this, LocationActivity.class);
				startActivity(intent);
			}
		});

		try {
			ExifInterface exifInterface = new ExifInterface(path);

			String latV = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
			String lngV = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

			// Log.d(TAG, latV);
			// Log.d(TAG, latRef);
			// Log.d(TAG, lngV);
			// Log.d(TAG, lngRef);

			float[] output = { 0f, 0f };
			exifInterface.getLatLong(output);
			// Log.d(TAG, output[0] + "");
			// Log.d(TAG, output[1] + "");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 发布操作
	private final OnClickListener onClickPublish = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FeedFormVO ffvo = new FeedFormVO();
			ffvo.setText(edit.getText().toString());
			ffvo.setImagePath(path);

			// 发布操作
			new FeedPublishTask(FeedPublishActivity.this).setListener(new PublishListener(FeedPublishActivity.this))
					.execute(ffvo);
		}

	};

}
