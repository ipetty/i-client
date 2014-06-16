package net.ipetty.android.feed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.vo.FeedFormVO;

public class FeedPublishActivity extends BaseActivity {

    private static final String TAG = "FeedPublishActivity";
    private String path;
    private DisplayImageOptions options;
    private EditText edit;
    private TextView btn_publish;
    private View location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_publish);

        /* action bar */
        ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
        TextView text = (TextView) this.findViewById(R.id.action_bar_title);
        text.setText(this.getResources().getString(R.string.title_activity_feed_publish));
        btnBack.setOnClickListener(new BackClickListener(this));

        options = AppUtils.getNormalImageOptions();

        // photoPath
        Intent intent = getIntent();
        path = intent.getStringExtra(Constant.INTENT_PHOTO_PATH_KEY);
        ImageView image = (ImageView) this.findViewById(R.id.image);
        String uri = Uri.fromFile(new File(path)).toString();
        ImageLoader.getInstance().displayImage(uri, image, options);

        edit = (EditText) this.findViewById(R.id.editText);
        btn_publish = (TextView) this.findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(onClickPublish);

        // location
        location = this.findViewById(R.id.location_layout);
        //选择位置
        location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //FeedPublishActivity.this.showMessageForShortTime("位置功能暂未实现");
                Intent intent = new Intent(FeedPublishActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }

    //发布操作
    private final OnClickListener onClickPublish = new OnClickListener() {
        @Override
        public void onClick(View v) {
            FeedFormVO ffvo = new FeedFormVO();
            ffvo.setText(edit.getText().toString());
            ffvo.setImagePath(path);

            // 发布操作
            new FeedPublishTask(FeedPublishActivity.this)
                    .setListener(new PublishListener(FeedPublishActivity.this))
                    .execute(ffvo);
        }

    };

}
