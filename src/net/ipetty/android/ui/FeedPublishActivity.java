package net.ipetty.android.ui;

import java.io.File;

import net.ipetty.R;
import net.ipetty.android.common.Constant;
import net.ipetty.android.utils.AppUtils;
import net.ipetty.android.utils.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FeedPublishActivity extends Activity {
	private static final String TAG = "FeedPublishActivity";
	private String path;
	private String outPath;
	private DisplayImageOptions options;
	private EditText edit;
	private TextView btn_publish;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_publish);
		options = AppUtils.getNormalImageOptions();

		// photoPath
		Intent intent = getIntent();
		path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		outPath = intent.getStringExtra(Constant.INTENT_PHOTO_OUT_PATH_KEY);
		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.fromFile(new File(path)).toString();
		ImageLoader.getInstance().displayImage(uri, image, options);

		edit = (EditText) this.findViewById(R.id.editText);
		btn_publish = (TextView) this.findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(onClickPublish);

	}

	private final OnClickListener onClickPublish = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Editable text = edit.getText();
			String str = text.toString();

			// 压缩图片
			ImageUtils.compressImage(path, outPath);
			// TODO publish

		}

	};
}
