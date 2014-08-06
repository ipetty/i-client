/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.update;

import android.util.Log;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import net.ipetty.android.core.Constant;

/**
 *
 * @author Administrator
 */
public class UpdateUtils {

	private static String TAG = UpdateUtils.class.getSimpleName();
	public static OkHttpClient client = new OkHttpClient();
	public static Gson gson = new Gson();
	private static String updateUrl = Constant.API_SERVER_BASE + "/files/update.json";

	public static UpdateVO getUpdateInfo() {
		Log.d(TAG, "getUpdateInfo");
		Request request = new Request.Builder()
				.url(updateUrl)
				.build();
		Response response = null;
		UpdateVO result = null;
		try {
			response = client.newCall(request).execute();
			Log.d(TAG, "response.isSuccessful = " + response.isSuccessful());
			if (response.isSuccessful()) {
				String jsonStr = response.body().toString();
				Log.d(TAG, "jsonStr = " + jsonStr);
				result = gson.fromJson(jsonStr, UpdateVO.class);
			}

		} catch (IOException ex) {
			Log.e(TAG, "", ex);
		}
		if (result == null) {
			Log.d(TAG, "result = null");
		} else {
			Log.d(TAG, "result.getVersionDescription = " + result.getVersionDescription());
		}

		return result;

	}

}
