/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.android.core.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

/**
 * 
 * @author Administrator
 */
public class FragmentLifeTest extends Fragment {

	private String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		Log.d(TAG, "onInflate");
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach");
		super.onAttach(activity);
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Log.d(TAG, "onCreateAnimation");
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		Log.d(TAG, "onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "onLowMemory");
		super.onLowMemory();
	}

	@Override
	public void onDestroyView() {
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();

		super.onDetach();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "onDetach");
		super.onDetach();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.d(TAG, "onCreateOptionsMenu");
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		Log.d(TAG, "onPrepareOptionsMenu");
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onDestroyOptionsMenu() {
		Log.d(TAG, "onDestroyOptionsMenu");
		super.onDestroyOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		Log.d(TAG, "onOptionsMenuClosed");
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		Log.d(TAG, "onCreateContextMenu");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d(TAG, "onContextItemSelected");
		return super.onContextItemSelected(item);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		Log.d(TAG, "onHiddenChanged");
		super.onHiddenChanged(hidden);
	}

}
