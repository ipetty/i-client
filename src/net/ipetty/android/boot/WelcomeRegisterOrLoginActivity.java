package net.ipetty.android.boot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.login.LoginActivity;

public class WelcomeRegisterOrLoginActivity extends BaseActivity {

	public final static String TAG = WelcomeRegisterOrLoginActivity.class.getSimpleName();

	private ViewPager viewPager;
	private ViewGroup group;

	private ImageView imageView;
	private ImageView[] imageViews;

	private int[] imgIdArray;
	private ImageView[] welcomeImageViews;
	private Button enterBtn;
	private TextView version;

	private TextSwitcher title_ts;
	private TextSwitcher ts;
	private String[] titleArray = {"记录", "分享", "VIP内测"};
	private String[] poemArray = {"让手机记录我的宠物生活", "选择精彩记录分享到社交网络", "VIP内测版本正式启用"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_register_or_login);
		Log.d(TAG, "onCreate");
	}

	//加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
		// 初始化页面
		initViews();
		// 初始化小圆点
		initDots();

		// 初始化按钮
		enterBtn = (Button) this.findViewById(R.id.enter_btn);
		enterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeRegisterOrLoginActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		version = (TextView) this.findViewById(R.id.version_info);
		String verStr = getResources().getString(R.string.app_version);
		String v = String.format(verStr, AppUtils.getAppVersionName(this));
		version.setText(v);
	}

	private void initViews() {

		imgIdArray = new int[]{R.drawable.welcome_pic1, R.drawable.welcome_pic2, R.drawable.welcome_pic3};

		welcomeImageViews = new ImageView[imgIdArray.length];
		for (int i = 0; i < welcomeImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgIdArray[i]);
			welcomeImageViews[i] = imageView;
		}

		viewPager = (ViewPager) findViewById(R.id.welcome_view_pager);
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(welcomePageChangeListener);

		title_ts = (TextSwitcher) findViewById(R.id.welcome_title);
		ts = (TextSwitcher) findViewById(R.id.welcome_pane_caption);

		ts.setFactory(new ViewFactory() {
			public View makeView() {
				TextView tv = new TextView(WelcomeRegisterOrLoginActivity.this);
				tv.setTextAppearance(WelcomeRegisterOrLoginActivity.this, R.style.welcome_caption);
				tv.setShadowLayer(2, 1, 1, R.color.welcome_black_shadow);
				tv.setGravity(Gravity.CENTER);
				return tv;
			}
		});

		title_ts.setFactory(new ViewFactory() {
			public View makeView() {
				TextView tv = new TextView(WelcomeRegisterOrLoginActivity.this);
				tv.setTextAppearance(WelcomeRegisterOrLoginActivity.this, R.style.welcome_title);
				tv.setShadowLayer(2, 1, 1, R.color.welcome_black_shadow);
				tv.setGravity(Gravity.CENTER);
				return tv;
			}
		});

		// ts.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		// android.R.anim.slide_in_left));
		// ts.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		// android.R.anim.slide_out_right));
		ts.setText(poemArray[0]);
		title_ts.setText(titleArray[0]);
	}

	private final PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return welcomeImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object arg2) {
			((ViewPager) container).removeView(welcomeImageViews[position % welcomeImageViews.length]);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(welcomeImageViews[position % welcomeImageViews.length], 0);
			Log.d("welcome", "position->" + position);
			return welcomeImageViews[position % welcomeImageViews.length];

		}

	};

	private final OnPageChangeListener welcomePageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			setCurrentDot(arg0);
			ts.setText(poemArray[arg0]);
			title_ts.setText(titleArray[arg0]);
		}

	};

	private void initDots() {
		group = (ViewGroup) findViewById(R.id.dotViewGroup);
		// 将小圆点放到imageView数组当中
		imageViews = new ImageView[welcomeImageViews.length];
		for (int i = 0; i < welcomeImageViews.length; i++) {
			imageView = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(24, 24);
			lp.setMargins(10, 0, 10, 0);
			imageView.setLayoutParams(lp);
			imageViews[i] = imageView;
			group.addView(imageViews[i]);
		}
		setCurrentDot(0);
	}

	// 设置当前的样式
	private void setCurrentDot(int arg) {
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[arg].setBackgroundResource(R.drawable.dot_focused);
			if (arg != i) {
				imageViews[i].setBackgroundResource(R.drawable.dot_normal);
			}
		}
	}

}
