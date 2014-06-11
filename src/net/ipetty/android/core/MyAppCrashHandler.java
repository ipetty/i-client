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
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.BaseFragmentActivity;
import net.ipetty.android.sdk.core.APIException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

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
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }
}
