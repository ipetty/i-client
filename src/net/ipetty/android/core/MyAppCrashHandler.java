/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.sdk.core.IpetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyAppCrashHandler implements UncaughtExceptionHandler {

	private String TAG = getClass().getSimpleName();

	private final Thread.UncaughtExceptionHandler mDefaultHandler;
	private final Context mContext;

	public MyAppCrashHandler(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG, ex.getMessage(), ex);
		String msg = "亲，出错了";
		showError(msg);
		collectDeviceInfo(thread, mContext);
		saveCrashInfo2File(ex);
		waitFor(3 * 1000);
		ActivityManager.getInstance().exit();
	}

	// 另启线程进行等待，防止阻塞UI线程
	private void waitFor(long time) {
		// 异步转同步
		final CountDownLatch latch = new CountDownLatch(1);
		final Long waitTime = time;
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException ex) {

				}
				latch.countDown();
			}
		}.start();
		try {
			latch.await();
		} catch (InterruptedException ex) {

		}
	}

	private void showError(final String msg) {
		// 非UI线程进行UI界面操作
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
	}

	private final Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分

	/**
	 * 收集设备参数信息
	 * 
	 * @param thread
	 * @param context
	 */
	public void collectDeviceInfo(Thread thread, Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
				info.put("threadName", thread.getName());
				Integer uid = IpetApi.init(mContext).getCurrUserId();
				info.put("currUserId", uid.toString());
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {
				Log.e(TAG, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}

	private String saveCrashInfo2File(Throwable ex) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key).append("=").append(value).append("\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		String result = writer.toString();
		sb.append(result);

		// 如果有外部存储则保存文件
		long timetamp = System.currentTimeMillis();
		String time = format.format(new Date());
		String fileName = "crash-" + time + "-" + timetamp + ".log";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				String dir = PathUtils.getCrashDir();
				FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
				return fileName;
			} catch (FileNotFoundException e) {
				Log.e(TAG, e.getMessage(), e);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		return null;
	}
}
