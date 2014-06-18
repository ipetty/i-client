package net.ipetty.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;

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
        String url = intent.getStringExtra("url");
        ImageView image = (ImageView) this.findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(url, image, options);
    }

}
