/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.lang.Thread.UncaughtExceptionHandler;

public class MyAppCrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = MyAppCrashHandler.class.getSimpleName();
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
    }

    private void showError(final String msg) {
        //非UI线程进行UI界面操作
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
