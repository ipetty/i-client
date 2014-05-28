/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.sdk.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author Administrator
 */
public class StateManager {

    private static final String TAG = "StateManager";
    //是否已登录
    private static final String IS_AUTHORIZED = "isAuthorized";
    //当前用户ID
    private static final String UID = "uid";
    
    private static final String USER_TOKEN = "user_token";
    
    private static final String REFRESH_TOKEN = "refresh_token";
    
    
    
    public static void setUserToken(Context ctx, String token) {
    	setString(ctx,USER_TOKEN,token);
    }

    public static String getUserToken(Context ctx) {
        return getString(ctx,USER_TOKEN);
    }
    
    public static void setRefreshToken(Context ctx, String token) {
    	setString(ctx,REFRESH_TOKEN,token);
    }

    public static String getRefreshToken(Context ctx) {
        return getString(ctx,REFRESH_TOKEN);
    }
    
    
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

    public static void setUid(Context ctx, Integer uid) {
        SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UID, uid);
        editor.commit();
    }

    public static Integer getUid(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        Integer uid = sp.getInt(UID, -1);
        return uid;
    }
    
    public static void setString(Context ctx, String key,String value){
    	SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getString(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String str = sp.getString(key, "");
        return str;
    }

}
