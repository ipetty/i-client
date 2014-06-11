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

    private static final String TAG = "MyAppCrashHandler";
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
        Log.i(TAG, "instanceof HttpClientErrorException:" + (ex instanceof HttpClientErrorException));
        Log.i(TAG, "instanceof HttpServerErrorException:" + (ex instanceof HttpServerErrorException));
        Log.i(TAG, "instanceof ResourceAccessException:" + (ex instanceof ResourceAccessException));
        Log.i(TAG, "instanceof APIException:" + (ex instanceof APIException));

        try {
            throw ex;
        } catch (HttpClientErrorException e) {
            Log.i(TAG, "catch HttpClientErrorException");
            msg = e.getResponseBodyAsString();
        } catch (HttpServerErrorException e) {
            Log.i(TAG, "catch HttpServerErrorException");
            msg = e.getResponseBodyAsString();
        } catch (ResourceAccessException e) {
            Log.i(TAG, "catch ResourceAccessException");
            msg = e.getMessage();
        } catch (RestClientException e) {
            Log.i(TAG, "catch RestClientException");
            msg = e.getMessage();
        } catch (Throwable e) {
            Log.i(TAG, "catch Throwable:" + e.getClass().getName());
        }
        showError(msg);

//        //HTTP客户端异常400
//        if (ex instanceof HttpClientErrorException) {
//            HttpClientErrorException e = (HttpClientErrorException) ex;
//            showError(e.getResponseBodyAsString());
//            return;
//        }
//
//        //HTTP服务端异常500
//        if (ex instanceof HttpServerErrorException) {
//            HttpServerErrorException e = (HttpServerErrorException) ex;
//            showError(e.getResponseBodyAsString());
//            return;
//        }
//
//        //HTTP资源访问（IO）异常
//        if (ex instanceof ResourceAccessException) {
//            ResourceAccessException e = (ResourceAccessException) ex;
//            showError(e.getMessage());
//            return;
//        }
//
//        //HTTP未知异常
//        if (ex instanceof RestClientException) {
//            RestClientException e = (RestClientException) ex;
//            showError(e.getMessage());
//            return;
//        }
//        //其它情况
//        showError(msg);
    }

    private void beforeShowErr() {
        BaseActivity ba = null;
        BaseFragmentActivity bfa = null;
        if (mContext instanceof BaseActivity) {
            ba = (BaseActivity) mContext;
        }
        if (mContext instanceof BaseFragmentActivity) {
            bfa = (BaseFragmentActivity) mContext;
        }
        if (ba != null) {
            ba.dismissProgressDialog();
        }
        if (bfa != null) {
            bfa.dismissProgressDialog();
        }
    }

    private void showError(final String msg) {
        beforeShowErr();
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }
}
