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
import net.ipetty.android.sdk.core.APIException;
import net.ipetty.android.sdk.core.ServiceUnavailableException;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

/**
 *
 * @author Administrator
 */
public class ErrorHandler {

	private final static String TAG = ErrorHandler.class.getSimpleName();

	private final Context context;

	public ErrorHandler(Context context) {
		this.context = context;
	}

	public void handleError(Throwable ex) {
		Log.d(TAG, "handleError");
		Log.e(TAG, "", ex);
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

		//服务器不可用异常
		if (ex instanceof ServiceUnavailableException) {
			showError("服务器维护中,请稍后使用");
			new DelayTask(context).setListener(new DefaultTaskListener<Void>(context) {
				@Override
				public void onSuccess(Void result) {
					ActivityManager.getInstance().exit();
				}
			}).execute(3 * 1000);

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

	//显示错误信息
	private void showError(final String msg) {
		Log.d(TAG, "showError");
		//支持非UI线程进行UI界面操作
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
	}
}
