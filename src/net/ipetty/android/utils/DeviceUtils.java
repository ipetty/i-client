package net.ipetty.android.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

public class DeviceUtils {

	public static final int REQUEST_CODE_PICK_IMAGE = 50;
	public static final int REQUEST_CODE_TAKE_IMAGE = 51;

	public static void chooserSysPics(Context context) {
		if (context == null) {
			return;
		}
		Intent localIntent = getChooserSysPicsIntent();
		((Activity) context).startActivityForResult(localIntent, REQUEST_CODE_PICK_IMAGE);
	}

	public static void chooserSysPics(Fragment fragment) {
		if (fragment.getActivity() == null) {
			return;
		}
		Intent localIntent = getChooserSysPicsIntent();
		fragment.startActivityForResult(localIntent, REQUEST_CODE_PICK_IMAGE);
	}

	public static Intent getChooserSysPicsIntent() {
		Intent localIntent = new Intent();
		localIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
		return localIntent;
	}

	// takePicture
	public static void takePicture(Context context) {
		takePicture(context, null, null);
	}

	public static void takePicture(Context context, String path, String filename) {
		if (context == null) {
			return;
		}
		Intent intent = getPhotoIntent(path, filename);
		((Activity) context).startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);

	}

	public static void takePicture(Fragment fragment) {
		takePicture(fragment, null, null);
	}

	public static void takePicture(Fragment fragment, String path, String filename) {
		if (fragment.getActivity() == null) {
			return;
		}
		Intent intent = getPhotoIntent(path, filename);
		fragment.startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);
	}

	public static Intent getPhotoIntent(String path, String filename) {
		if (StringUtils.isEmpty(path)) {
			path = PathUtils.getCarmerDir();
		}

		if (StringUtils.isEmpty(filename)) {
			filename = System.currentTimeMillis() + ".jpg";
		}
		File file = new File(path, filename);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		return intent;
	}

}
