package net.ipetty.android.core;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.BaseFragmentActivity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 * 异步任务基类,规范异步任务使用方法，与acivity基类配合使用
 *
 * @author xiao
 * @param <Params> 入参类型
 * @param <Result> 结果类型
 */
public abstract class MyAsyncTask<Params, Result> extends AsyncTask<Params, Integer, Result> {

    private final static String TAG = "MyAsyncTask";

    protected Boolean hasError = false;

    protected String errorMsg;

    protected String loadingMessage;

    protected Activity activity;

    protected BaseActivity baseActivity;

    protected BaseFragmentActivity baseFragmentActivity;

    //带有默认loading信息的构造
    public MyAsyncTask(Activity activity) {
        super();
        if (activity instanceof BaseActivity) {
            baseActivity = (BaseActivity) activity;
        }

        if (activity instanceof BaseFragmentActivity) {
            baseFragmentActivity = (BaseFragmentActivity) activity;
        }

        this.activity = activity;

        this.loadingMessage = activity.getResources().getString(R.string.app_loging);
    }

    //带有自定义loading信息的构造
    public MyAsyncTask(Activity activity, String loadingMessage) {
        this(activity);
        this.loadingMessage = loadingMessage;
    }

    //可以进度UI的更新
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog();
    }

    //不可进行UI更新
    @Override
    protected Result doInBackground(Params... args) {
        return myDoInBackground(args);
//        try {
//            return myDoInBackground(args);
//        } catch (HttpClientErrorException e) {				//HTTP客户端异常400
//            Log.i(TAG, "catch HttpClientErrorException");
//            hasError = true;
//            errorMsg = e.getResponseBodyAsString();
//            return null;
//        } catch (HttpServerErrorException e) {				//HTTP服务端异常500
//            Log.i(TAG, "catch HttpServerErrorException");
//            hasError = true;
//            errorMsg = e.getResponseBodyAsString();
//            return null;
//        } catch (ResourceAccessException e) {				//HTTP资源访问（IO）异常
//            Log.i(TAG, "catch ResourceAccessException");
//            hasError = true;
//            errorMsg = e.getMessage();
//            return null;
//        } catch (RestClientException e) {				//HTTP未知异常
//            Log.i(TAG, "catch RestClientException");
//            hasError = true;
//            errorMsg = e.getMessage();
//            return null;
//        } catch (Exception e) {                                         //其它异常
//            Log.i(TAG, "catch Exception");
//            hasError = true;
//            onError(Thread.currentThread(), e);
//            return null;
//        } finally {
//            if (hasError && errorMsg != null) {
//                showError(errorMsg);
//                return null;
//            }
//        }

    }

    //用于继承
    protected void onError(Thread thread, Exception ex) {
        Log.i(TAG, "onError");
    }

    //不可进行UI更新
    protected abstract Result myDoInBackground(Params... args);

    //可以进度UI的更新
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        dismissProgressDialog();
    }

    //显示进度框
    private void showProgressDialog() {
        if (baseActivity != null) {
            baseActivity.showProgressDialog(loadingMessage);
        }
        if (baseFragmentActivity != null) {
            baseFragmentActivity.showProgressDialog(loadingMessage);
        }
    }

    //隐藏进度框
    private void dismissProgressDialog() {
        if (baseActivity != null) {
            baseActivity.dismissProgressDialog();
        }
        if (baseFragmentActivity != null) {
            baseFragmentActivity.dismissProgressDialog();
        }
    }

    //显示错误信息
    private void showError(final String msg) {
        dismissProgressDialog();
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }

}
