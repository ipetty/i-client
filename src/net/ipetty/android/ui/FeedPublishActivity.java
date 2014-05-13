package net.ipetty.android.ui;

import java.io.File;

import net.ipetty.R;
import net.ipetty.android.common.Constant;
import net.ipetty.android.utils.AppUtils;
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
	private File photo;
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
		String path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
		photo = new File(path);
		ImageView image = (ImageView) this.findViewById(R.id.image);
		String uri = Uri.fromFile(photo).toString();
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
			// TODO publish

		}

	};
}
