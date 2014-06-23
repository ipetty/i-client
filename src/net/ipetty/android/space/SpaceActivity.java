package net.ipetty.android.space;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import net.ipetty.R;
import net.ipetty.android.bonuspoint.BonusPointActivity;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.discover.DiscoverAdapter;
import net.ipetty.android.fans.FansActivity;
import net.ipetty.android.follow.FollowsActivity;
import net.ipetty.android.petty.PettyActivity;
import net.ipetty.android.user.UserActivity;

public class SpaceActivity extends Activity {

    public final static String TAG = "SpaceActivity";
    private ViewFlipper viewFlipper;
    private View space_petty_view;
    private GridView space_photo_grid;
    private DiscoverAdapter space_photo_grid_adapter;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        userId = this.getIntent().getExtras().getInt("id");
        /* action bar */
        ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
        btnBack.setOnClickListener(new BackClickListener(this));

        String title = this.getResources().getString(R.string.title_activity_space);
        // 标题
        if (!isCurrentUser()) {
            String title_space = getResources().getString(R.string.title_space);
            String username = "张三";
            title = String.format(title_space, username);
        }
        TextView text = (TextView) this.findViewById(R.id.action_bar_title);
        text.setText(title);
        // 右侧操作按钮
        TextView action_bar_right_text = (TextView) this.findViewById(R.id.action_bar_right_text);
        if (isCurrentUser()) {
            action_bar_right_text.setText(R.string.user_edit);
            action_bar_right_text.setOnClickListener(userEditClick);
        } else {
            // TODO:判断用户是否关注
            if (true) {
                action_bar_right_text.setText(R.string.follow_text);
            } else {
                action_bar_right_text.setText(R.string.unfollow_text);
            }
            action_bar_right_text.setOnClickListener(toggleFollowClick);
        }

        // 头像
        ImageView avatar = (ImageView) this.findViewById(R.id.avatar);
        if (isCurrentUser()) {
            avatar.setOnClickListener(userEditClick);
        }

        View fans = findViewById(R.id.fans_layout);
        fans.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SpaceActivity.this, FansActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        View follows = findViewById(R.id.follows_layout);
        follows.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SpaceActivity.this, FollowsActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        View bonusPoint = findViewById(R.id.bonusPoint_layout);
        bonusPoint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SpaceActivity.this, BonusPointActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        //
        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(0);

        View space_petty_layout_btn = this.findViewById(R.id.space_petty_btn);
        View space_photo_layout_btn = this.findViewById(R.id.space_photo_btn);
        View space_feed_layout_btn = this.findViewById(R.id.space_feed_btn);

        space_petty_layout_btn.setOnClickListener(new TabClickListener(0));
        space_photo_layout_btn.setOnClickListener(new TabClickListener(1));
        space_feed_layout_btn.setOnClickListener(new TabClickListener(2));

        space_petty_view = this.findViewById(R.id.list_space_petty_item);
        View pet_edit_view = space_petty_view.findViewById(R.id.pet_edit_view);

        if (isCurrentUser()) {
            pet_edit_view.setVisibility(View.VISIBLE);
            pet_edit_view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(SpaceActivity.this, PettyActivity.class);
                    startActivity(intent);
                }
            });
        }

        // 图形
        View space_photo_layout = this.findViewById(R.id.space_photo_layout);
        space_photo_grid = (GridView) space_photo_layout.findViewById(R.id.gridview);
        space_photo_grid_adapter = new DiscoverAdapter(this);
        space_photo_grid.setAdapter(space_photo_grid_adapter);
    }

    public class TabClickListener implements OnClickListener {

        private int index = 0;

        public TabClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            viewFlipper.setDisplayedChild(index);
        }
    }

    private boolean isCurrentUser() {
        // TODO 判断是否当前用户
        return true;
    }

    private OnClickListener userEditClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SpaceActivity.this, UserActivity.class);
            startActivity(intent);
        }
    };

    private OnClickListener toggleFollowClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO 关注与反关注的操作
            TextView view = (TextView) v;
            Toast.makeText(SpaceActivity.this, "暂无", Toast.LENGTH_SHORT).show();
        }
    };

}
