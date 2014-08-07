/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core.ui;

import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.DelayTask;
import net.ipetty.android.core.ErrorHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;

/**
 * 
 * @author Administrator
 */
public class BaseFragment extends Fragment {

	protected String TAG = getClass().getSimpleName();

	private boolean isViewReady = false;

	private Bundle savedInstanceState;

	private ErrorHandler errorHandler;

	private final int delayTime = 500;

	public void showMessageForShortTime(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	public void showMessageForLongTime(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onActivityCreated(savedInstanceState);
		ShareSDK.initSDK(this.getActivity());
		// this.isViewReady = false;
		this.savedInstanceState = savedInstanceState;
		errorHandler = new ErrorHandler(this.getActivity());
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		new DelayTask(this).setListener(new DefaultTaskListener<Void>(this) {
			@Override
			public void onSuccess(Void result) {
				// 只调用一次onViewReady
				if (!isViewReady) {
					onViewReady(savedInstanceState);
					onViewResume();
					onViewStart();
				}
				isViewReady = true;
			}
		}).execute(delayTime);

		if (isViewReady) {
			try {
				onViewResume();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	// 每次onActivityCreated，只触发一次，否则界面会消失
	protected void onViewReady(Bundle savedInstanceState) {
		Log.d(TAG, "onViewReady");
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
		if (isViewReady) {
			try {
				onViewStart();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	// ready情况下调用
	protected void onViewStart() {
		Log.d(TAG, "onViewStart");
	}

	// ready情况下调用
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
	}

}
