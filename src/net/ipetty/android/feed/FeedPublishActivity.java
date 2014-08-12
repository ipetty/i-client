package net.ipetty.android.feed;

import android.content.Intent;
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
import java.io.File;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.LocationVO;
import org.apache.commons.lang3.StringUtils;

public class FeedPublishActivity extends BaseActivity {

	private String path;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private EditText edit;
	private TextView btn_publish;
	private View locationView;
	private TextView locationText;
	private LocationVO locationVO = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_publish);
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_feed_publish));
		btnBack.setOnClickListener(new BackClickListener(this));

		edit = (EditText) this.findViewById(R.id.editText);
		btn_publish = (TextView) this.findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(onClickPublish);

		// photoPath
		Intent intent = getIntent();
		path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		String t = intent.getStringExtra(Constant.INTENT_FeedText_KEY);
		if (StringUtils.isNotBlank(t)) {
			edit.setText(t);
		}

		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.decode(Uri.fromFile(new File(path)).toString());
		ImageLoader.getInstance().displayImage(uri, image, options);
		//locationVO
		String locationVOJson = intent.getStringExtra(Constant.INTENT_LOCATION_VO_KEY);
		if (StringUtils.isNotBlank(locationVOJson)) {
			locationVO = JSONUtils.fromJSON(locationVOJson, LocationVO.class);
		}

		//位置布局
		locationView = this.findViewById(R.id.location_layout);
		locationView.setVisibility(View.VISIBLE);
		locationView.setOnClickListener(new ViewOnClickListener());

		locationText = (TextView) this.findViewById(R.id.location_tips);
		String tmpStr = getResources().getString(R.string.publish_location_tips);

		if (locationVO == null) {
			String locatStr = String.format(tmpStr, "");
			locationText.setText(locatStr);
		} else {
			String locatStr = String.format(tmpStr, locationVO.getAddress());
			locationText.setText(locatStr);
		}

	}

	private class ViewOnClickListener implements OnClickListener {

		public void onClick(View view) {
			Intent intent = new Intent(FeedPublishActivity.this, LocationActivity.class);
			intent.putExtra(Constant.INTENT_PHOTO_PATH_KEY, path);
			intent.putExtra(Constant.INTENT_FeedText_KEY, edit.getText().toString());

			FeedPublishActivity.this.startActivity(intent);
			FeedPublishActivity.this.finish();
		}

	};

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		// location
		//location = this.findViewById(R.id.location_layout);
		// 选择位置
//		location.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// FeedPublishActivity.this.showMessageForShortTime("位置功能暂未实现");
//				Intent intent = new Intent(FeedPublishActivity.this, LocationActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		try {
//			ExifInterface exifInterface = new ExifInterface(path);
//
//			String latV = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//			String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//			String lngV = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//			String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//
//			// Log.d(TAG, latV);
//			// Log.d(TAG, latRef);
//			// Log.d(TAG, lngV);
//			// Log.d(TAG, lngRef);
//
//			float[] output = { 0f, 0f };
//			exifInterface.getLatLong(output);
//			// Log.d(TAG, output[0] + "");
//			// Log.d(TAG, output[1] + "");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	// 发布操作
	private final OnClickListener onClickPublish = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FeedFormVO ffvo = new FeedFormVO();
			ffvo.setText(edit.getText().toString());
			ffvo.setImagePath(path);
			if (locationVO != null) {
				ffvo.setLocation(locationVO);
			}

			// 发布操作
			new FeedPublishTask(FeedPublishActivity.this).setListener(new PublishListener(FeedPublishActivity.this))
					.execute(ffvo);
		}

	};

}
