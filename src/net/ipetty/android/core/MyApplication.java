package net.ipetty.android.core;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

        private String TAG = getClass().getSimpleName();
        public LocationClient mLocationClient = null;

        @Override
        public void onCreate() {
                // TODO Auto-generated method stub
                super.onCreate();
                Log.d(TAG, "onCreate");
                initImageLoader(getApplicationContext());
                Thread.setDefaultUncaughtExceptionHandler(new MyAppCrashHandler(this));
                //初始化百度定位SDK
                mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
                SDKInitializer.initialize(getApplicationContext());             //百度地图初始化
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
                option.setProdName("ipetty");//产品线名称
                option.setLocationNotify(false);//不进行位置提醒
                option.setCoorType(Constant.LOCATE_COOR_TYPE);//返回的定位结果是百度经纬度,默认值gcj02
                option.setScanSpan(0);//一次定位模式
                option.setIsNeedAddress(true);//返回的定位结果包含地址信息
                option.setNeedDeviceDirect(false);//返回的定位结果包含手机机头的方向
                option.setOpenGps(true);//如果用户已打开GPS，则可以使用GPS
                option.SetIgnoreCacheException(true);//不捕捉异常
                option.setTimeOut(5 * 1000);
                mLocationClient.setLocOption(option);
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
