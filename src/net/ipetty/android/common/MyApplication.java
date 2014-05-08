package net.ipetty.android.common;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	public final static String TAG = "MyApplication";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate");
		initImageLoader(getApplicationContext());

	}

	// imageLoader
	private void initImageLoader(Context context) {
		// File cacheDir =
		// StorageUtils.getOwnCacheDirectory(getApplicationContext(), "Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		// 线程优先级
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个
				.denyCacheImageMultipleSizesInMemory()
				// 自定义缓存路径
				// .diskCache(new UnlimitedDiscCache(cacheDir))
				// 缓存文件名
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 设置图片下载和显示的工作队列排序
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
