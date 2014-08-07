package net.ipetty.android.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import net.ipetty.R;

public class AppUtils {

	private static String sID = null;
	private static final String INSTALLATION = "INSTALLATION";

	//Activity跳转
	public static void goTo(Activity activity, Class<? extends Activity> clazz) {
		Intent intent = new Intent(activity, clazz);
		activity.startActivity(intent);
		AnimUtils.fadeInToOut(activity);
	}

	//获取应用本次安装ID，每次安装都不同：http://892848153.iteye.com/blog/1828565
	public synchronized static String getAppInstallId(Context context) {
		if (sID == null) {
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists()) {
					writeInstallationFile(installation);
				}
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation) throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static void writeInstallationFile(File installation) throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}

	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionCode;
	}

	public static Builder getCacheImageBublder() {
		Builder bulider = new DisplayImageOptions.Builder()//
				.cacheInMemory(true)//
				.cacheOnDisk(true)//
				.showImageOnFail(R.drawable.default_image)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.considerExifParams(true);
		return bulider;
	}

	public static DisplayImageOptions getNormalImageOptions() {
		DisplayImageOptions options = getCacheImageBublder().build();
		return options;
	}

	public static DisplayImageOptions getRoundedImageOptions() {
		return getRoundedImageOptions(20);
	}

	public static DisplayImageOptions getRoundedImageOptions(Integer i) {
		DisplayImageOptions options = getCacheImageBublder().displayer(new RoundedBitmapDisplayer(i)).build();
		return options;
	}
}
