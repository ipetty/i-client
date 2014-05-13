package net.ipetty.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AppUtils {
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

	public static Builder getCacheImageBublder() {
		Builder bulider = new DisplayImageOptions.Builder()//
				.cacheInMemory(true)//
				.cacheOnDisk(true)//
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
