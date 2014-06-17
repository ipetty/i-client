/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.BaseFragmentActivity;
import net.ipetty.android.sdk.core.APIException;
import org.apache.http.conn.ConnectTimeoutException;
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

        //应用异常 界面层
        if (ex instanceof AppException) {
            AppException e = (AppException) ex;
            showError(e.getMessage());
            return;
        }

        //超时
        if (ex instanceof ConnectTimeoutException) {
            ConnectTimeoutException e = (ConnectTimeoutException) ex;
            showError("请求超时，请检查网络后重试");
            return;
        }

        //API异常 任务层
        if (ex instanceof APIException) {
            APIException e = (APIException) ex;
            if (null == e.getMessage() || "".equals(e.getMessage())) {
                showError("未知异常");
            } else {
                showError(e.getMessage());
            }

            return;
        }

        //HTTP客户端异常400
        if (ex instanceof HttpClientErrorException) {
            HttpClientErrorException e = (HttpClientErrorException) ex;
            if (null == e.getMessage() || "".equals(e.getMessage())) {
                showError(e.getStatusText());
            } else {
                showError(e.getMessage());
            }
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
            showError("无法连接到服务器");
            return;
        }

        //HTTP未知异常
        if (ex instanceof RestClientException) {
            RestClientException e = (RestClientException) ex;
            showError("未知HTTP异常");
            return;
        }

        showError("未知任务异常");

    }

    //显示进度框
    private void showProgressDialog() {
        Log.d(TAG, "showProgressDialog");
        if (loadingMessage != null) {
            if (baseActivity != null) {
                baseActivity.showProgressDialog(loadingMessage);
            }
            if (baseFragmentActivity != null) {
                baseFragmentActivity.showProgressDialog(loadingMessage);
            }
        }
    }

    //隐藏进度框
    private void dismissProgressDialog() {
        Log.d(TAG, "dismissProgressDialog");
        if (loadingMessage != null) {
            if (baseActivity != null) {
                baseActivity.dismissProgressDialog();
            }
            if (baseFragmentActivity != null) {
                baseFragmentActivity.dismissProgressDialog();
            }
        }
    }

    //显示错误信息
    private void showError(final String msg) {
        Log.d(TAG, "showError");
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

}
