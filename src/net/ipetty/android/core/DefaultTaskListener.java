/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.BaseFragmentActivity;
import net.ipetty.android.sdk.core.APIException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author yneos
 * @param <Result> 结果类型
 */
public abstract class DefaultTaskListener<Result> implements TaskListener<Result> {

    private final static String TAG = DefaultTaskListener.class.getSimpleName();

    protected Activity activity;

    protected BaseActivity baseActivity;

    protected BaseFragmentActivity baseFragmentActivity;

    protected String loadingMessage;

    /**
     * 带有默认loading信息的构造
     *
     * @param activity
     */
    public DefaultTaskListener(Activity activity) {
        if (activity instanceof BaseActivity) {
            baseActivity = (BaseActivity) activity;
        }

        if (activity instanceof BaseFragmentActivity) {
            baseFragmentActivity = (BaseFragmentActivity) activity;
        }

        this.activity = activity;

        this.loadingMessage = activity.getResources().getString(R.string.app_loging);
    }

    /**
     * 带有自定义loading信息的构造
     *
     * @param activity
     * @param loadingMessage
     */
    public DefaultTaskListener(Activity activity, String loadingMessage) {
        this(activity);
        this.loadingMessage = loadingMessage;
    }

    public void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        showProgressDialog();

    }

    public void doSuccess(Result result) {
        Log.d(TAG, "doSuccess");
        dismissProgressDialog();
        onSuccess(result);
    }

    public abstract void onSuccess(Result result);

    public void onProgressUpdate(Integer... prgrss) {
        Log.d(TAG, "onProgressUpdate");
    }

    public void onCancelled(Object result) {
        dismissProgressDialog();
        Log.d(TAG, "onCancelled:result");
    }

    public void onCancelled() {
        dismissProgressDialog();
        Log.d(TAG, "onCancelled");
    }

    public void onError(Throwable ex) {
        Log.d(TAG, "onError:" + ex.getClass().getName());
        dismissProgressDialog();

        //API异常
        if (ex instanceof APIException) {
            APIException e = (APIException) ex;
            showError(e.getMessage());
            return;
        }

        //HTTP客户端异常400
        if (ex instanceof HttpClientErrorException) {
            HttpClientErrorException e = (HttpClientErrorException) ex;
            showError("HTTP请求失败");
            return;
        }

        //HTTP服务端异常500
        if (ex instanceof HttpServerErrorException) {
            HttpServerErrorException e = (HttpServerErrorException) ex;
            showError(e.getResponseBodyAsString());
            return;
        }

        //HTTP资源访问（IO）异常
        if (ex instanceof ResourceAccessException) {
            ResourceAccessException e = (ResourceAccessException) ex;
            showError("HTTP资源访问异常");
            return;
        }

        //HTTP未知异常
        if (ex instanceof RestClientException) {
            RestClientException e = (RestClientException) ex;
            showError("HTTP未知异常");
            return;
        }

        showError("未知的任务异常");

    }

    //显示进度框
    private void showProgressDialog() {
        Log.d(TAG, "showProgressDialog");
        if (baseActivity != null) {
            baseActivity.showProgressDialog(loadingMessage);
        }
        if (baseFragmentActivity != null) {
            baseFragmentActivity.showProgressDialog(loadingMessage);
        }
    }

    //隐藏进度框
    private void dismissProgressDialog() {
        Log.d(TAG, "dismissProgressDialog");
        if (baseActivity != null) {
            baseActivity.dismissProgressDialog();
        }
        if (baseFragmentActivity != null) {
            baseFragmentActivity.dismissProgressDialog();
        }
    }

    //显示错误信息
    private void showError(final String msg) {
        Log.d(TAG, "showError");
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
//        new Thread() {
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
    }

}
