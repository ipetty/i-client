package net.ipetty.android.ui;

import net.ipetty.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

public class MainNewsFragment extends Fragment {
	public final static String TAG = "MainNewsFragment";
	private Activity activity;
	private ViewFlipper viewFlipper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_fragment_news, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated");
		this.activity = this.getActivity();

		viewFlipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
		viewFlipper.setDisplayedChild(0);

		View related_me = activity.findViewById(R.id.related_me);
		View my_follows = activity.findViewById(R.id.my_follows);
		related_me.setOnClickListener(new TabClickListener(0));
		my_follows.setOnClickListener(new TabClickListener(1));
	}

	public class TabClickListener implements OnClickListener {
		private int index = 0;

		public TabClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewFlipper.setDisplayedChild(index);
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

}
