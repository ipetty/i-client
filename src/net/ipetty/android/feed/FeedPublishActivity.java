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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.MyApplication;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.vo.FeedFormVO;
import net.ipetty.vo.LocationVO;

public class FeedPublishActivity extends BaseActivity {

	private String path;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	private EditText edit;
	private TextView btn_publish;
	private View locationView;
	private TextView locationText;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
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
		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.decode(Uri.fromFile(new File(path)).toString());
		ImageLoader.getInstance().displayImage(uri, image, options);

		mLocationClient = ((MyApplication) getApplication()).mLocationClient;
		mLocationClient.registerLocationListener(myListener);    //注册监听函数

		locationView = this.findViewById(R.id.location_layout);
		locationView.setVisibility(View.VISIBLE);
		locationText = (TextView) this.findViewById(R.id.location_tips);
		String tmpStr = getResources().getString(R.string.publish_location_tips);
		String locatStr = String.format(tmpStr, "正在确定...");
		locationText.setText(locatStr);
		mLocationClient.start();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		mLocationClient.stop();
		super.onStop();
	}

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

	/**
	 * 实现实位回调监听
	 */
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d(TAG, "onReceiveLocation");
			String tmpStr = getResources().getString(R.string.publish_location_tips);
			if (location == null) {//定位失败
				String locatStr = String.format(tmpStr, "定位失败");
				locationText.setText(locatStr);
			} else {
				StringBuilder lo = new StringBuilder();
				lo.append(location.getCity());
				lo.append(",");
				lo.append(location.getDistrict());
				lo.append(",");
				lo.append(location.getStreet());
				String locatStr = String.format(tmpStr, lo.toString());
				locationText.setText(locatStr);

				locationVO = new LocationVO();
				locationVO.setCoorType(Constant.LOCATE_COOR_TYPE);
				locationVO.setSilent(false);

				locationVO.setLatitude(location.getLatitude());
				locationVO.setLongitude(location.getLongitude());
				locationVO.setProvince(location.getProvince());
				locationVO.setCity(location.getCity());
				locationVO.setDistrict(location.getDistrict());
				locationVO.setStreet(location.getStreet());
				locationVO.setStreetNumber(location.getStreetNumber());
				locationVO.setAddress(lo.toString());
				locationVO.setRadius(location.getRadius());
			}

			//Receive Location
			StringBuilder sb = new StringBuilder(256);
			sb.append("时间 : ");
			sb.append(location.getTime());
			sb.append("\n定位方式 : ");
			sb.append(location.getLocType());
			sb.append("\n纬度 : ");
			sb.append(location.getLatitude());
			sb.append("\n经度 : ");
			sb.append(location.getLongitude());
			sb.append("\n精度 : ");
			sb.append(location.getRadius());
			sb.append("\n省 : ");
			sb.append(location.getProvince());
			sb.append("\n市 : ");
			sb.append(location.getCity());
			sb.append("\n市编码 : ");
			sb.append(location.getCityCode());
			sb.append("\n区/县 : ");
			sb.append(location.getDistrict());
			sb.append("\n街道 : ");
			sb.append(location.getStreet());
			sb.append("\n门牌号 : ");
			sb.append(location.getStreetNumber());
			sb.append("\n详细地址 : ");
			sb.append(location.getAddrStr());
			sb.append("\n楼层 : ");
			sb.append(location.getFloor());
			Log.i(TAG, sb.toString());
		}

	}

}
