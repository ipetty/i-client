/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core;

import android.app.Activity;
import android.content.Context;

/**
 *
 * @author Administrator
 */
public class DelayTask extends Task<Integer, Void> {

	public DelayTask(Activity activity) {
		super(activity);
	}

	public DelayTask(Context context) {
		super(context);
	}

	@Override
	protected Void myDoInBackground(Integer... args) {
		try {
			Thread.sleep(args[0]);
		} catch (InterruptedException ex) {

		}
		return null;
	}

}
