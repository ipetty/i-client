package net.ipetty.android.ui;

import net.ipetty.R;
import net.ipetty.android.utils.AnimUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends BaseFragmentActivity {
	public final static String TAG = "MainActivity";
	private ViewPager viewPager;
	private ImageView mTabImg;
	private int currIndex = 0;
	private int zero = 0;
	private int one;
	private int two;
	private final Class[] fragments = { MainHomeFragment.class, MainDiscoverFragment.class, MainNewsFragment.class };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i(TAG, "onCreate");

		// sub_action_bar_now
		DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics(this);
		int screenWidth = dm.widthPixels;
		one = screenWidth / 3;
		two = one * 2;

		mTabImg = (ImageView) findViewById(R.id.imageTabNow);
		RelativeLayout.LayoutParams mParams = (LayoutParams) mTabImg.getLayoutParams();
		mParams.width = one;
		mTabImg.setLayoutParams(mParams);

		// pager
		viewPager = (ViewPager) findViewById(R.id.tabpager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setOnPageChangeListener(myPageChangeListener);
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
			super.destroyItem(container, position, object);
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
			switch (arg0) {
			case 0: {
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, zero, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, zero, 0, 0);
				}
				break;
			}
			case 1: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}

				break;
			}
			case 2: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}

				break;
			}

			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}
}
