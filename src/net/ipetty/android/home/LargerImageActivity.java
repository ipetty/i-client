package net.ipetty.android.home;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LargerImageActivity extends BaseActivity {

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_larger_image);

	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String url = intent.getStringExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY);
		ImageView image = (ImageView) this.findViewById(R.id.image);
		ImageLoader.getInstance().displayImage(url, image, options);
	}

}
