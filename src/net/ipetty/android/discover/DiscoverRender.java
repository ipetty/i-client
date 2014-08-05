package net.ipetty.android.discover;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.feed.SimpleFeedActivity;
import net.ipetty.vo.FeedVO;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DiscoverRender {
	private String TAG = getClass().getSimpleName();
	public int j = 0;
	public Activity activity;
	private LinearLayout layout1;
	private LinearLayout layout2;
	private LinearLayout layout3;
	private List<FeedVO> list = new ArrayList<FeedVO>(0); // 这个就本地dataStore
	private DisplayImageOptions options;
	private int width;

	public DiscoverRender(Activity activity) {
		this.activity = activity;
		DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics(this.activity);
		width = dm.widthPixels / 3;

		layout1 = (LinearLayout) this.activity.findViewById(R.id.layout1);
		layout2 = (LinearLayout) this.activity.findViewById(R.id.layout2);
		layout3 = (LinearLayout) this.activity.findViewById(R.id.layout3);
	}

	public void loadData(List<FeedVO> result) {
		list.clear();
		j = 0;
		layout1.removeAllViews();
		layout2.removeAllViews();
		layout3.removeAllViews();
		addData(result);
	}

	public void addData(List<FeedVO> result) {
		list.addAll(result);
		addImage(result);
	}

	private void addImage(List<FeedVO> result) {
		for (int i = 0; i < result.size(); i++) {
			addBitMapToImage(result.get(i), j, i);
			j++;
			if (j >= 3)
				j = 0;
		}
	}

	public ImageView getImageview() {
		// 创建显示图片的对象
		ImageView imageView = new ImageView(this.activity);
		LayoutParams layoutParams = new LayoutParams(width, width);
		layoutParams.setMargins(0, 0, 0, 2);
		imageView.setLayoutParams(layoutParams);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageResource(R.drawable.default_image);

		return imageView;
	}

	public void addBitMapToImage(FeedVO feed, int j, int i) {
		ImageView imageView = getImageview();
		imageView.setTag(i);
		if (j == 0) {
			layout1.addView(imageView);
		} else if (j == 1) {
			layout2.addView(imageView);
		} else if (j == 2) {
			layout3.addView(imageView);
		}

		final long id = feed.getId();
		final int pos = i;
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, SimpleFeedActivity.class);
				intent.putExtra(Constant.INTENT_FEED_ID_KEY, id);
				FeedVO feedVO = list.get(pos);
				intent.putExtra(Constant.FEEDVO_JSON_SERIALIZABLE, JSONUtils.toJson(feedVO).toString());
				activity.startActivity(intent);
			}
		});
		if (StringUtils.isNotBlank(feed.getImageSmallURL())) {
			String str = Constant.FILE_SERVER_BASE + feed.getImageSmallURL();
			Log.d(TAG, str);
			ImageLoader.getInstance().displayImage(str, imageView, options);
		}

	}

}
