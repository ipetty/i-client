/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import net.ipetty.android.core.Constant;

/**
 *
 * @author Administrator
 */
public class MessageService extends Service {

	private final static String TAG = MessageService.class.getSimpleName();

	private final int interval = 60 * 1000;

	private Thread getMessageThread;

	private boolean running = false;

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
		running = true;
		getMessageThread = new Thread() {
			@Override
			public void run() {
				try {
					while (running) {
						Thread.sleep(interval);
						//TODO:请求消息
						Intent intent = new Intent(Constant.BROADCAST_HAS_NEW_MESSAG);
						MessageService.this.sendBroadcast(intent);
						Log.d(TAG, "BROADCAST_HAS_NEW_MESSAG");
					}

				} catch (Exception ex) {
					running = false;
					Log.e(TAG, "消息服务异常", ex);
				}
			}
		};
		getMessageThread.start();

	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		running = false;
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, " onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, " onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
}
