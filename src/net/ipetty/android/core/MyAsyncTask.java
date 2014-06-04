package net.ipetty.android.core;

import net.ipetty.R;
import net.ipetty.android.core.ui.BaseActivity;
import android.os.AsyncTask;

/**
 * 异步任务基类,规范异步任务使用方法，与acivity基类配合使用
 * @author xiao
 *
 * @param <T> 结果类型
 */
public abstract class MyAsyncTask <P,T> extends AsyncTask<P, Integer, T> {
	public final static String TAG = "MyAsyncTask";
	
	protected BaseActivity activity;
	
	public MyAsyncTask(BaseActivity activity) {
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		String str = activity.getResources().getString(R.string.app_loging);
		activity.showProgressDialog(str);
	}


	@Override
	protected void onPostExecute(T result) {
		activity.dismissProgressDialog();
	}

	

}
