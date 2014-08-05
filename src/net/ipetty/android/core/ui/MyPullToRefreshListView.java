/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.ipetty.android.core.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyPullToRefreshListView extends PullToRefreshListView {

	protected String TAG = getClass().getSimpleName();

	public MyPullToRefreshListView(Context context) {
		super(context);
	}

	public MyPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyPullToRefreshListView(Context context, Mode mode) {
		super(context, mode);
	}

	public MyPullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	public void setStartHeaderRefresh() {
		// TODO Auto-generated method stub
	}
}
