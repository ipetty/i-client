package net.ipetty.android.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.MyApplication;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.vo.LocationVO;
import org.apache.commons.lang3.StringUtils;

public class LocationActivity extends BaseActivity {

	private View layout0;
	private View layout1;
	private View layout2;
	private View layout3;
	private View layout4;

	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private LocationVO locationVO = null;
	private String path;
	private String textStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_location));
		btnBack.setOnClickListener(new BackClickListener());

		layout0 = this.findViewById(R.id.layout0);
		layout1 = this.findViewById(R.id.layout1);
		layout2 = this.findViewById(R.id.layout2);
		layout3 = this.findViewById(R.id.layout3);
		layout4 = this.findViewById(R.id.layout4);

		path = this.getIntent().getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		textStr = this.getIntent().getStringExtra(Constant.INTENT_FeedText_KEY);
		mLocationClient = ((MyApplication) getApplication()).mLocationClient;
		mLocationClient.registerLocationListener(myListener);    //注册监听函数

		layout0.setVisibility(View.VISIBLE);
		TextView item0 = (TextView) layout0.findViewById(R.id.item);
		item0.setText("正在确定位置...");

	}

	//返回事件
	private void goBack() {
		Intent intent = new Intent(this, FeedPublishActivity.class);
		intent.putExtra(Constant.INTENT_PHOTO_PATH_KEY, path);
		intent.putExtra(Constant.INTENT_FeedText_KEY, textStr);
		if (locationVO != null) {
			intent.putExtra(Constant.INTENT_LOCATION_VO_KEY, JSONUtils.toJson(locationVO));
		}
		LocationActivity.this.startActivity(intent);
		this.finish();
	}

	//返回事件
	private class BackClickListener implements View.OnClickListener {

		public void onClick(View view) {
			goBack();
		}
	}

	//返回事件
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				goBack();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		mLocationClient.start();
	}

	/**
	 * 实现实位回调监听
	 */
	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			Log.d(TAG, "onReceiveLocation");
			if (location == null || StringUtils.isBlank(location.getCity())) {//定位失败
				TextView item0 = (TextView) layout0.findViewById(R.id.item);
				item0.setText("定位失败");
			} else {
				layout0.setVisibility(View.GONE);

				TextView item1 = (TextView) layout1.findViewById(R.id.item);
				item1.setText("无");
				layout1.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
						locationVO = null;
						goBack();
					}
				});
				layout1.setVisibility(View.VISIBLE);

				final TextView item2 = (TextView) layout2.findViewById(R.id.item);
				item2.setText(location.getCity());
				layout2.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
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
						locationVO.setAddress(item2.getText().toString());
						locationVO.setRadius(location.getRadius());
						goBack();
					}
				});
				layout2.setVisibility(View.VISIBLE);

				final TextView item3 = (TextView) layout3.findViewById(R.id.item);
				item3.setText(location.getCity() + " " + location.getDistrict());
				layout3.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
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
						locationVO.setAddress(item3.getText().toString());
						locationVO.setRadius(location.getRadius());
						goBack();
					}
				});
				layout3.setVisibility(View.VISIBLE);

				final TextView item4 = (TextView) layout4.findViewById(R.id.item);
				item4.setText(location.getCity() + " " + location.getDistrict() + " " + location.getStreet());
				layout4.setOnClickListener(new OnClickListener() {
					public void onClick(View view) {
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
						locationVO.setAddress(item4.getText().toString());
						locationVO.setRadius(location.getRadius());
						goBack();
					}
				});
				layout4.setVisibility(View.VISIBLE);

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
			Log.d(TAG, sb.toString());
		}

	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop");
		mLocationClient.stop();
		super.onStop();
	}

}
