package net.ipetty.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import net.ipetty.android.common.Constant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtils {
	private static final String TAG = "ImageUtils";

	public final static int RESULT_SUCCESS = 0; // 成功
	public final static int RESULT_FAILTRUE = 1; // 失败

	public static int compressImage(String path, String outPath) {
		long t1 = System.currentTimeMillis(); // 排序前取得当前时间
		Bitmap bitmap = compressImageZoom(path);
		long t2 = System.currentTimeMillis(); // 排序前取得当前时间
		bitmap = compressImageScaled(bitmap);
		long t3 = System.currentTimeMillis(); // 排序前取得当前时间
		int level = getCompressLevel(bitmap);
		long t4 = System.currentTimeMillis(); // 排序前取得当前时间
		Log.i(TAG, "缩小:" + (t2 - t1));
		Log.i(TAG, "等比:" + (t3 - t2));
		Log.i(TAG, "质量:" + (t4 - t3));
		Log.i(TAG, "总耗:" + (t4 - t1));
		File myCaptureFile = new File(outPath);
		try {
			FileOutputStream bos = new FileOutputStream(myCaptureFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, level, bos);
			bos.flush();
			bos.close();
			bitmap.recycle();
			return RESULT_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return RESULT_FAILTRUE;
		}

	}

	public static boolean isCorrectSize(String srcPath) {
		int[] size = getSize(srcPath);
		int w = size[0];
		int h = size[1];
		// 最小宽高
		float minW = Constant.COMPRESS_IMAGE_MIN_WIDTH;
		float minH = Constant.COMPRESS_IMAGE_MIN_HEIGHT;
		if (w < minW && h < minH) {
			return false;
		}
		return true;
	}

	public static int[] getSize(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		return new int[] { w, h };
	}

	public static Bitmap compressImageZoom(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		int[] size = getSize(srcPath);
		int w = size[0];
		int h = size[1];
		float ww = Constant.COMPRESS_IMAGE_MAX_WIDTH;
		float hh = Constant.COMPRESS_IMAGE_MAX_HEIGHT;
		// 缩放比
		int be = 1;// be=1表示不缩放
		be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0) {
			be = 1;
		}
		Log.i(TAG, "Zoom:" + be);
		newOpts.inSampleSize = be;// 设置缩放比例 这里有个问题 貌似如果是
		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565; // 是否采用这个方案待评估
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	public static int getCompressLevel(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > Constant.COMPRESS_IMAGE_KB && options > 20) { // 压缩后图片过大，继续压缩
			baos.reset();// 即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;// 每次都减少10
		}
		Log.i(TAG, "level" + options);
		return options;
	}

	public static Bitmap compressImageMemorySize(Bitmap image) {
		int level = getCompressLevel(image);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, level, baos);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	public static Bitmap compressImageScaled(Bitmap bm) {
		int w = bm.getWidth();
		int h = bm.getHeight();
		float ww = Constant.COMPRESS_IMAGE_MAX_WIDTH;
		float hh = Constant.COMPRESS_IMAGE_MAX_HEIGHT;
		int reqWidth = w;
		if (h > w && w < ww) {
			return bm;
		}
		if (w > h && w < hh) {
			return bm;
		}

		if (h > w) {
			reqWidth = (int) ww;
		}
		if (w > h) {
			reqWidth = (int) hh;
		}
		int reqHeight = (int) (reqWidth * h) / w;
		Bitmap bitmap = Bitmap.createScaledBitmap(bm, reqWidth, reqHeight, true);
		return bitmap;
	}
}
