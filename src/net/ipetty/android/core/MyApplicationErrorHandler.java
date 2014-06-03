/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import java.lang.Thread.UncaughtExceptionHandler;

import net.ipetty.android.sdk.core.APIException;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class MyApplicationErrorHandler implements UncaughtExceptionHandler {

    private static final String TAG = "ActivityErrorHandler";
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private final Context mContext;

    public  MyApplicationErrorHandler(Context context) {
    	mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
    	Log.e(TAG, ex.getMessage(), ex);
    	String msg = "亲，出错了";
    	if (ex instanceof APIException ){
    		msg = ex.getMessage();
    	}
    	final String errMsg = msg;
    	
    	new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, errMsg,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }

}
