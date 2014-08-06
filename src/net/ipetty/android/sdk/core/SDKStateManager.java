/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.core;

import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 
 * @author Administrator
 */
public class SDKStateManager {

	private static final String TAG = SDKStateManager.class.getSimpleName();

	// 是否已登录
	private static final String IS_AUTHORIZED = "isAuthorized";
	// 当前用户ID
	private static final String UID = "uid";

	private static final String USER_TOKEN = "user_token";

	private static final String REFRESH_TOKEN = "refresh_token";

	private static final String DEVICE_UUID = "device_uuid";

	private static final String CURRENT_USER_INFO = "current_user_info";

	// 第三方帐号登录时的第三方平台名称
	private static final String PLATFORM_NAME = "platform_name";

	// 当前用户信息
	public static void setCurrentUserInfo(Context ctx, UserVO user) {
		Log.d(TAG, "setCurrentUserInfo");
		if (user == null) {
			return;
		}
		String str = JSONUtils.toJson(user);
		setString(ctx, CURRENT_USER_INFO, str);
	}

	public static UserVO getCurrentUserInfo(Context ctx) {
		Log.d(TAG, "getCurrentUserInfo");
		String str = getString(ctx, CURRENT_USER_INFO);
		if (StringUtils.isBlank(str)) {
			return null;
		}
		UserVO user = JSONUtils.fromJSON(str, UserVO.class);
		return user;
	}

	// 设备UUID
	public static void setDeviceUUID(Context ctx, String str) {
		Log.d(TAG, "setDeviceUUID:" + str);
		setString(ctx, DEVICE_UUID, str);
	}

	public static String getDeviceUUID(Context ctx) {
		String str = getString(ctx, DEVICE_UUID);
		Log.d(TAG, "getDeviceUUID:" + str);
		return str;
	}

	// 用户Token
	public static void setUserToken(Context ctx, String token) {
		Log.d(TAG, "setUserToken:" + token);
		setString(ctx, USER_TOKEN, token);
	}

	public static String getUserToken(Context ctx) {
		String str = getString(ctx, USER_TOKEN);
		Log.d(TAG, "getUserToken:" + str);
		return str;
	}

	// 刷新Token
	public static void setRefreshToken(Context ctx, String token) {
		Log.d(TAG, "setRefreshToken:" + token);
		setString(ctx, REFRESH_TOKEN, token);
	}

	public static String getRefreshToken(Context ctx) {
		String str = getString(ctx, REFRESH_TOKEN);
		Log.d(TAG, "getRefreshToken:" + str);
		return str;
	}

	// 验证状态
	public static boolean getAuthorized(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		boolean ret = sp.getBoolean(IS_AUTHORIZED, false);
		return ret;
	}

	public static void setAuthorized(Context ctx, boolean isAuthorized) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(IS_AUTHORIZED, isAuthorized);
		editor.commit();
	}

	// 第三方平台
	public static String getPlatformName(Context ctx) {
		String platformName = getString(ctx, PLATFORM_NAME);
		Log.d(TAG, "getPlatformName:" + platformName);
		return platformName;
	}

	public static void setPlatformName(Context ctx, String platformName) {
		if (platformName != null) {
			Log.d(TAG, "setPlatformName:" + platformName);
			setString(ctx, PLATFORM_NAME, platformName);
		}
	}

	// 用户ID
	public static void setUid(Context ctx, Integer uid) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(UID, uid);
		editor.commit();
	}

	public static Integer getUid(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		Integer uid = sp.getInt(UID, Constant.EMPTY_USER_ID);
		return uid;
	}

	protected static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	protected static String getString(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		String str = sp.getString(key, "");
		return str;
	}

}
