package net.ipetty.android.user;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.PathUtils;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends BaseActivity {
	public final static String TAG = "UserActivity";
	private ImageView avatar;
	private String mImageName = "cacheHead.jpg";
	private Dialog dialog;
	private Dialog sexDialog;
	private String genderValue;

	private EditText gender;
	private EditText birthday;
	private Dialog birthdayDialog;
	private List<ModDialogItem> sexyItems;

	private static final int REQUEST_CODE_PHOTORESOULT = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_user));
		btnBack.setOnClickListener(new BackClickListener(this));

		TextView save = (TextView) this.findViewById(R.id.save);
		save.setOnClickListener(saveClick);

		avatar = (ImageView) this.findViewById(R.id.avatar);
		avatar.setOnClickListener(avatarClick);

		sexyItems = new ArrayList<ModDialogItem>();
		sexyItems.add(new ModDialogItem(null, "1", "男", sexOnClick));
		sexyItems.add(new ModDialogItem(null, "2", "女", sexOnClick));

		gender = (EditText) this.findViewById(R.id.gender);
		gender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sexDialog = DialogUtils.modPopupDialog(UserActivity.this, sexyItems, sexDialog);
			}
		});

		birthday = (EditText) this.findViewById(R.id.birthday);
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				birthdayDialog = DialogUtils.datePopupDialog(UserActivity.this, onDateSetListener, birthday.getText().toString(), birthdayDialog);
			}
		});
	}

	private OnDateSetListener onDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String str = format.format(c.getTime());
			birthday.setText(str);

		}

	};

	private OnClickListener sexOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
			String val = ((TextView) v.findViewById(R.id.value)).getText().toString();
			gender.setText(text);
			genderValue = val;
			Log.i(TAG, "v->" + genderValue);
			sexDialog.cancel();
		}
	};

	private OnClickListener saveClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener avatarClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showCameraDialog(v);
		}
	};

	public void showCameraDialog(View v) {
		OnClickListener[] Listener = new OnClickListener[] { takePhotoClick, pickPhotoClick };
		this.dialog = DialogUtils.bottomPopupDialog(this, Listener, R.array.alert_camera, getString(R.string.camera_title), this.dialog);
	}

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceUtils.takePicture(UserActivity.this, PathUtils.getCarmerDir(), UserActivity.this.mImageName);
			UserActivity.this.dialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			DeviceUtils.chooserSysPics(UserActivity.this);
			UserActivity.this.dialog.cancel();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("Photo", "finish");
		Log.i("Photo", "requestCode" + requestCode);
		Log.i("Photo", "resultCode" + resultCode);

		String path = PathUtils.getCarmerDir() + this.mImageName;
		File picture = new File(path);
		Uri pathUri = Uri.fromFile(picture);

		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				Log.i("Photo", "finish" + uri);
				startPhotoZoom(uri, pathUri);
			}
		}

		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Log.i("Photo", "finish" + picture);
				startPhotoZoom(Uri.fromFile(picture), pathUri);
			}

		}
		if (requestCode == REQUEST_CODE_PHOTORESOULT) {
			Log.i("Photo", "crop" + pathUri);
			avatar.setImageURI(pathUri);
		}

	}

	public void startPhotoZoom(Uri uri, Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("return-data", false);
		startActivityForResult(intent, REQUEST_CODE_PHOTORESOULT);
	}

}
