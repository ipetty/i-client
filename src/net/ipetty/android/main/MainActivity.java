package net.ipetty.android.main;

import net.ipetty.R;
import net.ipetty.android.core.ActivityManager;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.ui.BaseFragmentActivity;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.discover.MainDiscoverFragment;
import net.ipetty.android.home.MainHomeFragment;
import net.ipetty.android.news.MainNewsFragment;
import net.ipetty.android.service.MessageService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseFragmentActivity {

	private ViewPager viewPager;
	private ImageView mTabImg;
	private int currIndex = 0;
	private int zero = 0;
	private int one;
	private int two;
	private final Class[] fragments = { MainHomeFragment.class, MainDiscoverFragment.class, MainNewsFragment.class };

	private TextView main_text;
	private TextView discover_text;
	private TextView news_text;
	private ImageView news_dot;

	private int gray;
	private int red;

	private final Intent messageServiceIntent = new Intent(MessageService.class.getName());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate");

		// action_bar
		ImageView rightBtn = (ImageView) findViewById(R.id.action_bar_right_image);
		rightBtn.setOnClickListener(popMenuOnClick);
		// sub_action_bar_now
		DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics(this);
		int screenWidth = dm.widthPixels;
		one = screenWidth / 3;
		two = one * 2;

		mTabImg = (ImageView) findViewById(R.id.imageTabNow);
		RelativeLayout.LayoutParams mParams = (LayoutParams) mTabImg.getLayoutParams();
		mParams.width = one;
		mTabImg.setLayoutParams(mParams);
	}

	// 加载数据
	@Override
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");

		// pager
		viewPager = (ViewPager) findViewById(R.id.tabpager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setOnPageChangeListener(myPageChangeListener);

		// viewTab
		View main = findViewById(R.id.main);
		View discover = findViewById(R.id.discover);
		View news = findViewById(R.id.news);

		main_text = (TextView) main.findViewById(R.id.textView);
		discover_text = (TextView) discover.findViewById(R.id.textView);
		news_text = (TextView) news.findViewById(R.id.textView);

		news_dot = (ImageView) news.findViewById(R.id.dot);

		news_dot = (ImageView) news.findViewById(R.id.dot);

		main.setOnClickListener(new TabClickListener(0));
		discover.setOnClickListener(new TabClickListener(1));
		news.setOnClickListener(new TabClickListener(2));

		gray = getResources().getColor(R.color.title_gray);
		red = getResources().getColor(R.color.title_red);

		// 启动服务
		startService(messageServiceIntent);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.BROADCAST_HAS_NEW_MESSAG);
		this.registerReceiver(broadcastreciver, filter);
	}

	public class TabClickListener implements OnClickListener {

		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(index);
		}

	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		private Class[] fragments;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyFragmentPagerAdapter(FragmentManager fm, Class[] fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			try {
				fragment = (Fragment) fragments[position].newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
		}
	}

	private final OnPageChangeListener myPageChangeListener = new OnPageChangeListener() {
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
			Animation animation = null;

			main_text.setTextColor(gray);
			discover_text.setTextColor(gray);
			news_text.setTextColor(gray);

			switch (arg0) {
			case 0: {
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, zero, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, zero, 0, 0);
				}
				main_text.setTextColor(red);
				break;

			}
			case 1: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				discover_text.setTextColor(red);
				break;
			}
			case 2: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				news_text.setTextColor(red);
				// hideNewsDot();// TODO: just test
				break;
			}

			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

	};

	public OnClickListener popMenuOnClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(MainActivity.this, MainPopDialog.class);
			startActivity(intent);
		}
	};

	public void showNewsDot() {
		toggleNewsDot(true);
	}

	public void hideNewsDot() {
		toggleNewsDot(false);
	}

	public void toggleNewsDot(boolean bl) {
		if (bl) {
			news_dot.setVisibility(View.VISIBLE);
		} else {
			news_dot.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ShareSDK.stopSDK(this);
		// 停止服务
		stopService(messageServiceIntent);
		unregisterReceiver(broadcastreciver);
		Log.d(TAG, "onDestroy");
	}

	private BroadcastReceiver broadcastreciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (Constant.BROADCAST_HAS_NEW_MESSAG.equals(action)) {
				showNewsDot();
			}

		}
	};

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				String exit_once_again = getResources().getString(R.string.exit_once_again);
				Toast.makeText(this, exit_once_again, Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				ActivityManager.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

}
