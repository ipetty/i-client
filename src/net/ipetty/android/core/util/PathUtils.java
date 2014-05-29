package net.ipetty.android.core.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class PathUtils {
	public static final String CRASH_DIR = "crash";
	public static final String DB_DIR = "databases";
	public static final String IPETTY_APP_DIR = "net.ipetty";
	public static final String SAVE_PICTURE_IN_SDCARD = "camera";
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	static {
		if (existExternalStorageDirectory()) {
			File appFile = new File(getExternalStorageDirectory(), IPETTY_APP_DIR);
			if (!appFile.exists())
				appFile.mkdirs();
		}
	}

	public static File createFile(String url, String filename) {
		File file = null;
		if ((StringUtils.isEmpty(filename)) || (!existExternalStorageDirectory())) {
			return file;
		}

		if (StringUtils.isEmpty(url)) {
			File dir = createAppDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			file = new File(dir, filename);
			if (file.exists() && file.isFile()) {
				return file;
			}
			try {
				file.createNewFile();
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return file;
	}

	public static File createAppDir() {
		File file = new File(getExternalStorageDirectory() + File.separator + IPETTY_APP_DIR);
		if (!existsPath_ExternalStorageDirectory(IPETTY_APP_DIR)) {
			file.mkdirs();
		}
		return file.getAbsoluteFile();
	}

	public static String getCarmerDir() {
		File file = new File(createAppDir() + File.separator + SAVE_PICTURE_IN_SDCARD + File.separator);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath() + File.separator;
	}

	public static String getExternalStorageDirectory() {
		if (existExternalStorageDirectory()) {
			return SDCARD_PATH;
		} else {
			return null;
		}
	}

	public static boolean existExternalStorageDirectory() {
		return ("mounted".equals(Environment.getExternalStorageState())) && (Environment.getExternalStorageDirectory().canWrite());
	}

	public static boolean existsPath_ExternalStorageDirectory(String url) {
		if (StringUtils.isEmpty(url)) {
			return false;
		}

		if (existExternalStorageDirectory()) {
			return new File(getExternalStorageDirectory() + File.separator + url).exists();
		}

		return false;
	}

	public static String getPathByUri(Uri uri, Context paramContext) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = paramContext.getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	public static String getPathByUriFromFile(Uri uri, Context paramContext) {

		if ((!StringUtils.isEmpty(uri.toString())) && (uri.toString().contains("file://"))) {
			String path = uri.getPath();
			String sdPath = PathUtils.SDCARD_PATH;
			if ((!StringUtils.isEmpty(sdPath)) && (sdPath.contains("/mnt")) && (!StringUtils.isEmpty(path)) && (!path.startsWith("/mnt"))) {
				path = "/mnt" + path;
			}
			return path;
		}
		return getPathByUri(uri, paramContext);
	}

}
