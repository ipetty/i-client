package net.ipetty.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import net.ipetty.android.common.Constant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class ImageUtils {
	private static final String TAG = "ImageUtils";

	public final static int RESULT_SUCCESS = 0; // 成功
	public final static int RESULT_FAILTRUE = 1; // 失败
	public final static int RESULT_ERROR = 2; // 异常

	public static int compressImage(String path, String outPath) {
		Log.i(TAG, outPath + "outPath");
		Bitmap bitmap = compressImageZoom(path);
		bitmap = compressImageMatrix(bitmap);
		if (bitmap == null) {
			return RESULT_FAILTRUE;
		}
		File myCaptureFile = new File(outPath);
		try {
			FileOutputStream bos = new FileOutputStream(myCaptureFile);
			int level = 80; // getCompressLevel(Bitmap image) //性能问题暂不启用
			bitmap.compress(Bitmap.CompressFormat.JPEG, level, bos);
			bos.flush();
			bos.close();
			bitmap.recycle();
			return RESULT_SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return RESULT_ERROR;
		}

	}

	public static Bitmap compressImageZoom(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		int w = newOpts.outWidth;
		int h = newOpts.outHeight;

		// 最小宽高
		float minW = Constant.COMPRESS_IMAGE_MIN_WIDTH;//
		float minH = Constant.COMPRESS_IMAGE_MIN_HEIGHT;//
		if (w < minW && h < minH) {
			return null;
		}

		float ww = Constant.COMPRESS_IMAGE_MAX_WIDTH;//
		float hh = Constant.COMPRESS_IMAGE_MAX_HEIGHT;//
		// 缩放比
		int be = 1;// be=1表示不缩放
		/*
		 * if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放 be = (int) (w / ww); }
		 * else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放 be = (int) (h / hh);
		 * }
		 */
		// 替换上述算法
		be = (int) ((w / ww + h / hh) / 2);
		if (be <= 0)
			be = 1;
		Log.i(TAG, "press" + be);
		newOpts.inSampleSize = be;// 设置缩放比例 这里有个问题 貌似如果是
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inJustDecodeBounds = false;
		newOpts.inPreferredConfig = Bitmap.Config.ARGB_4444; // 是否采用这个方案待评估
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	public static int getCompressLevel(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		while (baos.toByteArray().length / 1024 > Constant.COMPRESS_IMAGE_KB) { // 压缩后图片过大，继续压缩
			int size = baos.toByteArray().length / 1024;
			Log.i(TAG, "level" + options + "size" + size);
			baos.reset();// 即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;// 每次都减少10
		}
		Log.i(TAG, options + "options");
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

	public static Bitmap compressImageMatrix(Bitmap bm) {
		int w = bm.getWidth();
		int h = bm.getHeight();
		float ww = Constant.COMPRESS_IMAGE_MAX_WIDTH;
		float hh = Constant.COMPRESS_IMAGE_MAX_HEIGHT;
		if (w < ww && h < hh) {
			return bm;
		}

		// 缩放图片的尺寸
		float scaleWidth = (float) ww / w;
		float scaleHeight = (float) hh / h;
		Matrix matrix = new Matrix();
		if (w > h) {
			scaleWidth = (float) ww / h;
			scaleHeight = (float) hh / w;
		}
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, false);
		return bitmap;
	}
}
