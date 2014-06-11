/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author yneos
 * @param <Params> 参数类型
 * @param <Result> 结果类型
 * @异步任务特性
 * @1.只参在UI线程中启动
 * @2.doInBackground在新线程(非UI线程)中执行,其它的on开头方法均在UI线程中执行
 * @3.非UI线程不能进行UI元素的更新操作
 * @4.一个AsyncTask实例只能使用一次
 */
public abstract class Task<Params, Result> extends AsyncTask<Params, Integer, Result> {

    //这样方便重构
    private final static String TAG = Task.class.getSimpleName();

    protected TaskListener<Result> listener;
    protected Activity activity;

    //存放异常
    protected Throwable ex;

    /**
     *
     * @param activity
     * @param listener
     */
    public Task(Activity activity, TaskListener<Result> listener) {
        super();
        this.activity = activity;
        this.listener = listener;
    }

    //UI线程
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        super.onPreExecute();
        listener.onPreExecute();
    }

    //非UI线程
    @Override
    protected Result doInBackground(Params... paramss) {
        Log.d(TAG, "doInBackground");
        try {
            return myDoInBackground(paramss);
        } catch (Throwable e) {
            ex = e;
            Log.i(TAG, "Error Calss Name:" + e.getClass().getName());
            String msg = e.getMessage() == null ? "" : e.getMessage();
            Log.e(TAG, msg, e);
            return null;
        }
    }

    /**
     * 非UI线程，后台执行内容
     *
     * @param args
     * @return
     */
    protected abstract Result myDoInBackground(Params... args);

    //UI线程
    @Override
    protected void onPostExecute(Result result) {
        Log.d(TAG, "onPostExecute");
        super.onPostExecute(result);
        if (result != null) {
            listener.onSuccess(result);
        } else {
            listener.onError(ex);
        }
    }

    //UI线程
    @Override
    protected void onProgressUpdate(Integer... prgrss) {
        Log.d(TAG, "onProgressUpdate");
        super.onProgressUpdate(prgrss);
        listener.onProgressUpdate(prgrss);
    }

    //UI线程
    @Override
    protected void onCancelled(Result result) {
        Log.d(TAG, "onCancelled:Result");
        super.onCancelled(result);
        listener.onCancelled(result);
    }

    //UI线程
    @Override
    protected void onCancelled() {
        Log.d(TAG, "onCancelled");
        super.onCancelled();
        listener.onCancelled();
    }

}
