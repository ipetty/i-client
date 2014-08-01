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
import java.util.UUID;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.util.JSONUtils;
import net.ipetty.android.core.util.NetWorkUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.NotificationVO;

/**
 *
 * @author Administrator
 */
public class MessageService extends Service {

	private final static String TAG = MessageService.class.getSimpleName();

	private final int interval = 60 * 1000;

	private Thread getMessageThread;

	private boolean running = false;

	private final UUID uuid = UUID.randomUUID();

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		super.onCreate();
		running = true;
		if (getMessageThread == null) {
			Log.d(TAG, "getMessageThread == null");
			getMessageThread = new Thread() {
				@Override
				public void run() {
					try {
						while (running) {
							if (NetWorkUtils.isNetworkConnected(MessageService.this)) {
								NotificationVO nvo = IpetApi.init(MessageService.this).getNotificationApi().getMyNotification();
								Log.d(TAG, uuid + ":接收到nvo:" + JSONUtils.toJson(nvo));
								if (nvo.getNewFansNum() > 0
										|| nvo.getNewFavorsNum() > 0
										|| nvo.getNewRepliesNum() > 0) {
									Intent intent = new Intent(Constant.BROADCAST_HAS_NEW_MESSAG);
									String nvoJsonStr = JSONUtils.toJson(nvo);
									intent.putExtra(Constant.BROADCAST_DATA, nvoJsonStr);
									MessageService.this.sendBroadcast(intent);
									Log.d(TAG, uuid + ":Send new messag:" + nvoJsonStr);
								}
							}
							Thread.sleep(interval);
						}

					} catch (Exception ex) {
						Log.e(TAG, "消息服务异常", ex);
					}
				}
			};
			getMessageThread.start();
		}

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
