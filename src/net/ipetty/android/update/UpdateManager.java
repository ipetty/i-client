package net.ipetty.android.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import java.io.File;

public class UpdateManager {
	/* 下载中 */

	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;

	/* 本版更新对象 */
	//TODO:
	//private static IpetAppUpdate updateInfo;

	/* 下载保存路径 */
	private String mSavePath;
	/* 下载文件名 */
	private final String mFileName = "ipet.apk";

	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private final Activity mContext;

	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					installApk();
					break;
				default:
					break;
			}
		}
	;

	};

	public UpdateManager(Activity context) {
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		if (isUpdate()) {
			// 显示提示对话框
			showNoticeDialog();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 */
	private boolean isUpdate() {
		//TODO:
//		final CountDownLatch latch = new CountDownLatch(1);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				MyApplication application = (MyApplication) mContext.getApplication();
//				updateInfo = application.getApi().getAppApi().checkAppVersion(Constant.APP_KEY);
//				latch.countDown();
//			}
//		}).start();
//
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		// 获取当前软件版本
//		int versionCode = getVersionCode(mContext);
//
//		if (updateInfo.getVersionCode() > versionCode) {
//			return true;
//		}
		return false;

	}

	/**
	 * 获取软件版本号
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// TODO：
//		AlertDialog.Builder builder = new Builder(mContext);
//		builder.setTitle(R.string.soft_update_title);
//		builder.setMessage(updateInfo.getUpdateText());
//		// 更新
//		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				// 显示下载对话框
//				showDownloadDialog();
//			}
//		});
//		// 稍后更新
//		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		Dialog noticeDialog = builder.create();
//		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// TODO：
//		AlertDialog.Builder builder = new Builder(mContext);
//		builder.setTitle(R.string.soft_updating);
//		// 给下载对话框增加进度条
//		final LayoutInflater inflater = LayoutInflater.from(mContext);
//		View v = inflater.inflate(R.layout.softupdate_progress, null);
//		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//		builder.setView(v);
//		// 取消更新
//		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				// 设置取消状态
//				cancelUpdate = true;
//			}
//		});
//		mDownloadDialog = builder.create();
//		mDownloadDialog.show();
//		// 下载文件
//		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 */
	private class downloadApkThread extends Thread {
		//TODO:
//		@Override
//		public void run() {
//			try {
//				// 判断SD卡是否存在，并且是否具有读写权限
//				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//					// 获得存储卡的路径
//					String sdpath = Environment.getExternalStorageDirectory() + "/";
//					mSavePath = sdpath + "download";
//					URL url = new URL(updateInfo.getAppDownloadUrl());
//					// 创建连接
//					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//					conn.connect();
//					// 获取文件大小
//					int length = conn.getContentLength();
//					// 创建输入流
//					InputStream is = conn.getInputStream();
//
//					File file = new File(mSavePath);
//					// 判断文件目录是否存在
//					if (!file.exists()) {
//						file.mkdir();
//					}
//					File apkFile = new File(mSavePath, mFileName);
//					FileOutputStream fos = new FileOutputStream(apkFile);
//					int count = 0;
//					// 缓存
//					byte buf[] = new byte[1024];
//					// 写入到文件中
//					do {
//						int numread = is.read(buf);
//						count += numread;
//						// 计算进度条位置
//						progress = (int) (((float) count / length) * 100);
//						// 更新进度
//						mHandler.sendEmptyMessage(DOWNLOAD);
//						if (numread <= 0) {
//							// 下载完成
//							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//							break;
//						}
//						// 写入文件
//						fos.write(buf, 0, numread);
//					} while (!cancelUpdate);// 点击取消就停止下载.
//					fos.close();
//					is.close();
//				}
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// 取消下载对话框显示
//			mDownloadDialog.dismiss();
//		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mFileName);
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
