package net.ipetty.android.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;

public class LargerImageActivity extends BaseActivity {

	private String original_url;
	private String small_url;
	private ImageView image;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_larger_image);
		Intent intent = getIntent();
		original_url = intent.getStringExtra(Constant.INTENT_IMAGE_ORIGINAL_KEY);
		small_url = intent.getStringExtra(Constant.INTENT_IMAGE_SAMILL_KEY);
		image = (ImageView) this.findViewById(R.id.image);
		Uri uri = Uri.fromFile(ImageLoader.getInstance().getDiskCache().get(small_url));
		image.setImageURI(uri);
		ImageLoader.getInstance().displayImage(original_url, image, options);
	}

}
