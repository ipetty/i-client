/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.DelayTask;
import net.ipetty.android.core.ErrorHandler;

/**
 *
 * @author Administrator
 */
public class BaseFragment extends Fragment {

	private String TAG = BaseFragment.class.getSimpleName(); //getClass().getSimpleName();

	private boolean isViewReady = false;

	private Bundle savedInstanceState;

	private ErrorHandler errorHandler;

	public void showMessageForShortTime(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	public void showMessageForLongTime(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
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
				//只调用一次onViewReady
				if (!isViewReady) {
					onViewReady(savedInstanceState);
					onViewResume();
					onViewStart();
				}
				isViewReady = true;
			}
		}).execute(500);

		if (isViewReady) {
			try {
				onViewResume();
			} catch (Throwable e) {
				errorHandler.handleError(e);
			}
		}
	}

	//界面初始化完毕，只触发一次
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

	//ready情况下调用
	protected void onViewStart() {
		Log.d(TAG, "onViewStart");
	}

	//ready情况下调用
	protected void onViewResume() {
		Log.d(TAG, "onViewResume");
	}

}
