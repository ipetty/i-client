/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用持久化状态
 *
 * @author yneos
 */
public class MyAppStateManager {

    private static final String TAG = MyAppStateManager.class.getSimpleName();
    //拍照时临时文件的整路径
    private static final String CAMERA_TEMP_FILE = "CAMERA_TEMP_FILE";

    //home页列表最后刷新时间
    private static final String LAST_REFRESH_FOR_HOME = "LAST_REFRESH_FOR_HOME";

    //设置拍照时临时文件完整路径，默认为空。
    public static void setCameraTempFile(Context ctx, String token) {
        setString(ctx, CAMERA_TEMP_FILE, token);
    }

    //获取拍照时临时文件完整路径
    public static String getCameraTempFile(Context ctx) {
        return getString(ctx, CAMERA_TEMP_FILE);
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
