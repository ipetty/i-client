/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * 
 * @author yneos
 * @param <Result>
 *            结果类型
 */
public abstract class DefaultTaskListener<Result> implements TaskListener<Result> {

	protected String TAG = getClass().getSimpleName();

	protected Activity activity;

	private String loadingMessage;

	private ProgressDialog progressDialog;

	private final ErrorHandler errorHandler;

	/**
	 * 带有默认loading信息的构造
	 * 
	 * @param activity
	 */
	public DefaultTaskListener(Activity activity) {
		this.activity = activity;
		errorHandler = new ErrorHandler(activity);
	}

	public DefaultTaskListener(Context context) {
		this((Activity) context);
	}

	public DefaultTaskListener(Fragment fragment) {
		this(fragment.getActivity());
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

		this.progressDialog = new ProgressDialog(this.activity);
		this.progressDialog.setIndeterminate(true);
		this.progressDialog.setCancelable(false);
	}

	public DefaultTaskListener(Fragment fragment, String loadingMessage) {
		this(fragment.getActivity());
	}

	public void onPreExecute() {
		Log.d(TAG, "onPreExecute");
		showProgressDialog();

	}

	public void doSuccess(Result result) {
		Log.d(TAG, "doSuccess");
		dismissProgressDialog();
		try {
			onSuccess(result);
		} catch (Throwable e) {
			onError(e);
		}
	}

	public abstract void onSuccess(Result result);

	public void onProgressUpdate(Integer... prgrss) {
		Log.d(TAG, "onProgressUpdate");
	}

	public void onCancelled(Result result) {
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

		errorHandler.handleError(ex);

	}

	// 显示进度框
	private void showProgressDialog() {
		Log.d(TAG, "showProgressDialog");
		if (getLoadingMessage() != null) {
			this.progressDialog.setMessage(getLoadingMessage());
			this.progressDialog.show();
		}
	}

	// 隐藏进度框
	private void dismissProgressDialog() {
		Log.d(TAG, "dismissProgressDialog");
		if (getLoadingMessage() != null) {
			this.progressDialog.dismiss();
		}
	}

	/**
	 * @return the loadingMessage
	 */
	public String getLoadingMessage() {
		return loadingMessage;
	}

}
