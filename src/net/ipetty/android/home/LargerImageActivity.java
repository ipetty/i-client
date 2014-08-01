package net.ipetty.android.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class LargerImageActivity extends BaseActivity {

	private String TAG = LargerImageActivity.class.getSimpleName();

	private String original_url;
	private String small_url;
	private ImageView image;
	//手势放大组件
	private PhotoViewAttacher mAttacher;
	private DisplayImageOptions options = AppUtils.getNormalImageOptions();
	//加载大图前默认透明度
	private int initAlpha = 155;

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_larger_image);

		Intent intent = getIntent();
		original_url = intent.getStringExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY);
		small_url = intent.getStringExtra(Constant.INTENT_IMAGE_SAMILL_KEY);
		image = (ImageView) this.findViewById(R.id.image);
		//设置初始图片
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		image.setAlpha(initAlpha);
		//image.setImageResource(R.drawable.default_image);

		//同步加载小图
		image = (ImageView) this.findViewById(R.id.image);
		Uri uri = Uri.fromFile(ImageLoader.getInstance().getDiskCache().get(small_url));
		image.setImageURI(uri);
	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		//异步加载大图
		ImageLoader.getInstance().displayImage(original_url, image, options, new LoadListener(), new progressListener());
	}

	private class progressListener implements ImageLoadingProgressListener {

		public void onProgressUpdate(String imageUri, View view, int current, int total) {
			Log.d(TAG, "current=" + current + ",total=" + total + ",persent:" + current / total);
			int persent = (current / total) * 100;
			if (persent > initAlpha) {
				image.setAlpha(initAlpha);

			}
			Log.d(TAG, "当前Alpha:" + 255 * persent);
		}
	}

	private class LoadListener implements ImageLoadingListener {

		public void onLoadingStarted(String imageUri, View view) {
			Log.d(TAG, "onLoadingStarted:");
		}

		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			Log.d(TAG, "onLoadingFailed:");
		}

		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			Log.d(TAG, "onLoadingComplete:");
			mAttacher = new PhotoViewAttacher(image);
			image.setAlpha(initAlpha);

		}

		public void onLoadingCancelled(String imageUri, View view) {
			Log.d(TAG, "onLoadingCancelled:");
		}

	}
}
