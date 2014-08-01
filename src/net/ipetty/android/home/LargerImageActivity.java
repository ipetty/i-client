package net.ipetty.android.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
	private PhotoViewAttacher mAttacher;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

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
		image.setImageResource(R.drawable.default_image);
	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		//同步加载小图
		Bitmap smallImage = ImageLoader.getInstance().loadImageSync(small_url, options);
		image.setImageBitmap(smallImage);
		//同步加载大图
		ImageLoader.getInstance().displayImage(original_url, image, options, new LoadListener());
	}

	private class LoadListener implements ImageLoadingListener {

		public void onLoadingStarted(String imageUri, View view) {

		}

		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

		}

		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			mAttacher = new PhotoViewAttacher(image);
		}

		public void onLoadingCancelled(String imageUri, View view) {

		}

	}
}
