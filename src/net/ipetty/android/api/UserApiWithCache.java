/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import java.util.concurrent.CountDownLatch;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.sdk.core.APIException;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author yneos
 */
public class UserApiWithCache {

	private final static String TAG = UserApiWithCache.class.getSimpleName();
	private static UserVO userForGetUserById4Synchronous;
	private static final UserVOCache cache = new UserVOCache(100);

	/**
	 * 同步获取用户
	 *
	 * @param context
	 * @param userId
	 * @return
	 */
	public static synchronized UserVO getUserById4Synchronous(final Context context, final Integer userId) {
		Log.d(TAG, "getUserById4Synchronous:" + userId);
		UserVO user = cache.get(userId);
		if (null != user) {
			Log.d(TAG, "getUserById4Synchronous:找到缓存," + userId + ",Avatar:" + user.getAvatar());
			return user;
		}
		Log.d(TAG, "getUserById4Synchronous:没找到缓存," + userId);
		final CountDownLatch latch = new CountDownLatch(1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				userForGetUserById4Synchronous = IpetApi.init(context).getUserApi().getById(userId);
				latch.countDown();
			}
		}).start();
		try {
			latch.await();
		} catch (InterruptedException ex) {
			throw new APIException(ex);
		}
		Log.d(TAG, "getUserById4Synchronous:put," + userId + ",Avatar:" + userForGetUserById4Synchronous.getAvatar());
		cache.put(userForGetUserById4Synchronous);
		return userForGetUserById4Synchronous;

	}

	public static synchronized void getUserById4Asynchronous(final Context context, final Integer userId,
			final DefaultTaskListener<UserVO> listenner) {
		Log.d(TAG, "getUserById4Asynchronous:" + userId);
		UserVO user = cache.get(userId);
		if (null != user) {
			Log.d(TAG, "getUserById4Asynchronous:找到缓存," + userId + ",Avatar:" + user.getAvatar());
			listenner.onSuccess(user);
			return;
		}
		Log.d(TAG, "getUserById4Asynchronous:没有找到缓存," + userId);
		String loadingMsg = listenner.getLoadingMessage();
		DefaultTaskListener<UserVO> myListener = null;
		if (StringUtils.isEmpty(loadingMsg)) {
			myListener = new DefaultTaskListener<UserVO>((Activity) context) {
				@Override
				public void onSuccess(UserVO result) {
					UserApiWithCache.cache.put(result);
					Log.d(TAG, "getUserById4Asynchronous:put," + userId + ",Avatar:" + result.getAvatar());
					listenner.onSuccess(result);
				}
			};
		} else {
			myListener = new DefaultTaskListener<UserVO>((Activity) context, loadingMsg) {
				@Override
				public void onSuccess(UserVO result) {
					Log.d(TAG, "getUserById4Asynchronous:put," + userId + ",Avatar:" + result.getAvatar());
					UserApiWithCache.cache.put(result);
					listenner.onSuccess(result);
				}
			};
		}

		new GetUserById((Activity) context).setListener(myListener).execute(userId);
	}

	public static synchronized void updateCache(UserVO user) {
		Log.d(TAG, "updateCache:" + user.getId());
		cache.put(user);
	}

	public static synchronized void removeCache(Integer id) {
		Log.d(TAG, "removeCache:" + id);
		cache.remove(id);
	}
}
