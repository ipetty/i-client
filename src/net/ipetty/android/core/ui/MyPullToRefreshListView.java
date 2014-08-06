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

import net.ipetty.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MyPullToRefreshListView extends PullToRefreshListView {

	protected String TAG = getClass().getSimpleName();
	private LayoutInflater mInflater;
	private RelativeLayout mMoreView;
	private TextView mText;
	protected ImageView mImage;
	protected ProgressBar mProgress;

	private int top;
	private int bottom;

	public MyPullToRefreshListView(Context context) {
		super(context);
		this.initFooter(context);
	}

	public MyPullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initFooter(context);
	}

	public MyPullToRefreshListView(Context context, Mode mode) {
		super(context, mode);
		this.initFooter(context);
	}

	public MyPullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
		this.initFooter(context);
	}

	private void initFooter(Context context) {
		mInflater = (LayoutInflater) LayoutInflater.from(context);
		mMoreView = (RelativeLayout) mInflater.inflate(R.layout.pull_to_refreshing, getRefreshableView(), false);
		top = mMoreView.getPaddingTop();
		bottom = mMoreView.getPaddingBottom();
		mText = (TextView) mMoreView.findViewById(R.id.pull_to_refresh_text);
		mText.setText(R.string.pull_to_refresh_refreshing_label);
		mText.setVisibility(View.VISIBLE);
		mImage = (ImageView) mMoreView.findViewById(R.id.pull_to_refresh_image);
		mImage.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.default_ptr_rotate));
		RotateAnimation mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setDuration(1200);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mImage.startAnimation(mRotateAnimation);
		getRefreshableView().addFooterView(mMoreView);
		setMode(Mode.DISABLED);
	}

	public interface OnLoadMoreListener {
		public void LoadMore();
	}

	public void onFirstLoaded() {
		setMode(Mode.PULL_FROM_START);
	}

	public void showMoreView() {
		// TODO Auto-generated method stub
		onFirstLoaded();
		mMoreView.setPadding(0, top, 0, bottom);
		mMoreView.setVisibility(View.VISIBLE);
	}

	public void hideMoreView() {
		// TODO Auto-generated method stub
		onFirstLoaded();
		mMoreView.setVisibility(View.GONE);
		mMoreView.setPadding(0, -mMoreView.getHeight(), 0, 0);

	}
}
