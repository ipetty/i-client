/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.util.concurrent.TimeUnit;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.NetWorkUtils;

/**
 *
 * @author Administrator
 */
public class UpdateUtils {

	private static final String TAG = UpdateUtils.class.getSimpleName();
	private static final OkHttpClient client = new OkHttpClient();
	private static final Gson gson = new Gson();
	private static final String updateUrl = Constant.FILE_SERVER_BASE + "/files/update.json";

	private static UpdateVO updateInfo;

	public static UpdateVO getUpdaeInfo() {
		return updateInfo;
	}

	/**
	 * 检查软件是否有更新版本
	 */
	public static boolean hasUpdate(Context context) {

		if (!NetWorkUtils.isNetworkConnected(context)) {
			return false;
		}
		//服务器无法连接
//		if (!IpetApi.init(context).checkServiceAvaliable()) {
//			throw new ServiceUnavailableException();
//		}

		updateInfo = checkUpdateInfo();

		// 获取当前软件版本
		int versionCode = getVersionCode(context);

		if (updateInfo == null || updateInfo.getVersionCode() == null) {
			return false;
		}

		if (updateInfo.getVersionCode() > versionCode) {
			return true;
		}
		return false;

	}

	private static UpdateVO checkUpdateInfo() {
		Log.d(TAG, "checkUpdateInfo");
		Request request = new Request.Builder()
				.url(updateUrl)
				.build();
		client.setConnectTimeout(3, TimeUnit.SECONDS);
		Response response = null;
		updateInfo = null;
		try {
			response = client.newCall(request).execute();
			Log.d(TAG, "response.isSuccessful = " + response.isSuccessful());
			if (response.isSuccessful()) {
				updateInfo = gson.fromJson(response.body().charStream(), UpdateVO.class);
			}

		} catch (Exception ex) {
			Log.e(TAG, "", ex);
			throw new RuntimeException(ex);
		}
		return updateInfo;
	}

	/**
	 * 获取软件版本号
	 */
	private static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "", e);
		}
		return versionCode;
	}

}
