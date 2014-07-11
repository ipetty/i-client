package net.ipetty.android.user;

import android.annotation.SuppressLint;
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
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.foundation.GetOptionValueLabelMap;
import net.ipetty.android.sdk.task.foundation.ListOptions;
import net.ipetty.android.sdk.task.user.UpdateUser;
import net.ipetty.android.sdk.task.user.UpdateUserAvatar;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.UserFormVO;
import net.ipetty.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

public class UserActivity extends BaseActivity {

	public static final String TAG = UserActivity.class.getSimpleName();

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Integer currUserId;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private TextView title; // 标题
	private ImageView backButton; // 返回
	private TextView saveButton; // 保存

	private ImageView avatar; // 头像
	private String mImageName = "cacheHead.jpg"; // 默认头像值
	private Dialog changeAvatarDialog; // 更换头像对话框

	private EditText nickname; // 昵称
	private TextView email; // 邮箱地址

	private EditText signature; // 个性签名

	private Dialog genderDialog; // 性别选择对话框
	private EditText gender; // 性别展现
	private String genderValue; // 性别传值

	private Dialog birthdayDialog; // 生日选择对话框
	private EditText birthday; // 生日

	private EditText stateAndRegion; // 地区

	private static final int REQUEST_CODE_PHOTORESOULT = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		currUserId = IpetApi.init(this).getCurrUserId();

		// Title & Back
		title = (TextView) this.findViewById(R.id.action_bar_title);
		title.setText(this.getResources().getString(R.string.title_activity_user));
		backButton = (ImageView) this.findViewById(R.id.action_bar_left_image);
		backButton.setOnClickListener(new BackClickListener(this));

		// 保存
		saveButton = (TextView) this.findViewById(R.id.save);
		saveButton.setOnClickListener(saveClick);

		// 头像
		avatar = (ImageView) this.findViewById(R.id.avatar);
		avatar.setOnClickListener(changeAvatarClick);

		// 昵称
		nickname = (EditText) this.findViewById(R.id.nickname);

		// 邮箱地址
		email = (TextView) this.findViewById(R.id.email);

		// 个性签名
		signature = (EditText) this.findViewById(R.id.description);

		// 性别
		gender = (EditText) this.findViewById(R.id.gender);
		new ListOptions(UserActivity.this).setListener(new ListOptionsTaskListener(UserActivity.this)).execute(
				OptionGroup.HUMAN_GENDER);

		// 生日
		birthday = (EditText) this.findViewById(R.id.birthday);
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				birthdayDialog = DialogUtils.datePopupDialog(UserActivity.this, selectBirthdayClick, birthday.getText()
						.toString(), birthdayDialog);
			}
		});

		// 地区
		stateAndRegion = (EditText) this.findViewById(R.id.city);

		// 填充用户信息
		UserApiWithCache.getUserById4Asynchronous(this, currUserId, new DefaultTaskListener<UserVO>(UserActivity.this) {
			@Override
			public void onSuccess(UserVO user) {
				// 头像
				if (StringUtils.isNotEmpty(user.getAvatar())) {
					Log.i(TAG, "设置用户头像：" + Constant.FILE_SERVER_BASE + user.getAvatar());
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar,
							options);
				} else {
					avatar.setImageResource(R.drawable.avatar);
				}

				// 昵称
				UserActivity.this.nickname.setText(user.getNickname() == null ? "" : user.getNickname());

				// 邮箱地址
				UserActivity.this.email.setText(user.getEmail() == null ? "" : user.getEmail());

				// 个性签名
				UserActivity.this.signature.setText(user.getSignature() == null ? "" : user.getSignature());

				// 性别
				if (StringUtils.isNotBlank(user.getGender())) {
					UserActivity.this.setGenderValue(user.getGender());
					new GetOptionValueLabelMap(UserActivity.this).setListener(
							new DefaultTaskListener<Map<String, String>>(UserActivity.this) {
								@Override
								public void onSuccess(Map<String, String> optionValueLabelMap) {
									String label = optionValueLabelMap.get(UserActivity.this.genderValue);
									UserActivity.this.gender.setText(label);
								}
							}).execute(OptionGroup.HUMAN_GENDER);
				}

				// 生日
				if (user.getBirthday() != null) {
					UserActivity.this.birthday.setText(dateFormat.format(user.getBirthday()));
				}

				// 地区
				UserActivity.this.stateAndRegion.setText(user.getStateAndRegion() == null ? "" : user
						.getStateAndRegion());
			}
		});

	}

	/**
	 * 更改头像
	 */
	private OnClickListener changeAvatarClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			showCameraDialog(view);
		}
	};

	/**
	 * 选择生日
	 */
	private OnDateSetListener selectBirthdayClick = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			String str = dateFormat.format(c.getTime());
			birthday.setText(str);
		}
	};

	/**
	 * 保存
	 */
	private OnClickListener saveClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			UserFormVO user = new UserFormVO();
			user.setId(UserActivity.this.currUserId);

			// nickname
			String nickname = UserActivity.this.nickname.getText().toString();
			user.setNickname(nickname);
			if (StringUtils.isEmpty(nickname)) {
				UserActivity.this.nickname.requestFocus();
				Toast.makeText(UserActivity.this, R.string.nickname_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			// gender
			user.setGender(UserActivity.this.genderValue);

			// stateAndRegion
			user.setStateAndRegion(UserActivity.this.stateAndRegion.getText().toString());

			// signature
			user.setSignature(UserActivity.this.signature.getText().toString());

			// birthday
			String birthday = UserActivity.this.birthday.getText().toString();
			if (StringUtils.isNotBlank(birthday)) {
				try {
					user.setBirthday(dateFormat.parse(birthday));
				} catch (ParseException e) {
					Log.e(TAG, e.getMessage());
				}
			}

			new UpdateUser(UserActivity.this)
					.setListener(
							new DefaultTaskListener<UserVO>(UserActivity.this, UserActivity.this
									.getString(R.string.submitting)) {
								@Override
								public void onSuccess(UserVO result) {
									Toast.makeText(activity, R.string.save_success, Toast.LENGTH_SHORT).show();
									// 更新缓存
									UserApiWithCache.updateCache(result);
								}
							}).execute(user);
		}
	};

	public void showCameraDialog(View view) {
		OnClickListener[] Listener = new OnClickListener[]{takePhotoClick, pickPhotoClick};
		this.changeAvatarDialog = DialogUtils.bottomPopupDialog(this, Listener, R.array.alert_camera,
				getString(R.string.camera_title), this.changeAvatarDialog);
	}

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.takePicture(UserActivity.this, PathUtils.getCarmerDir(), UserActivity.this.mImageName);
			UserActivity.this.changeAvatarDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.chooserSysPics(UserActivity.this);
			UserActivity.this.changeAvatarDialog.cancel();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "finish");
		Log.i(TAG, "requestCode" + requestCode);
		Log.i(TAG, "resultCode" + resultCode);

		String path = PathUtils.getCarmerDir() + this.mImageName;
		File picture = new File(path);
		Uri pathUri = Uri.fromFile(picture);

		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				Log.i(TAG, "finish" + uri);
				startPhotoZoom(uri, pathUri);
			}
		}
		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Log.i(TAG, "finish" + picture);
				startPhotoZoom(Uri.fromFile(picture), pathUri);
			}

		}
		if (requestCode == REQUEST_CODE_PHOTORESOULT) {
			Log.i(TAG, "crop" + pathUri);

			avatar.setImageURI(pathUri);
			updateAvatar(picture.getAbsolutePath());
		}
	}

	public void updateAvatar(final String filePath) {
		new UpdateUserAvatar(this).setListener(new DefaultTaskListener<String>(this) {
			@Override
			public void onSuccess(String result) {
				Log.i(TAG, "updateAvatar.onSuccess:" + result);
				UserApiWithCache.removeCache(currUserId);
				UserVO user = UserApiWithCache.getUserById4Synchronous(UserActivity.this, currUserId);
				if (StringUtils.isNotBlank(user.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
				} else {
					avatar.setImageResource(R.drawable.avatar);
				}
				IpetApi.init(UserActivity.this).setCurrUserInfo(user);
				UserActivity.this.showMessageForLongTime("更新头像成功");
			}

			@Override
			public void onError(Throwable ex) {
				super.onError(ex);
				UserActivity.this.showMessageForLongTime("更新头像失败");
				avatar.setImageResource(R.drawable.avatar);
				// ImageLoader.getInstance().displayImage(R.drawable.avatar,
				// avatar, options);
			}
		}).execute(filePath);
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

	public Dialog getGenderDialog() {
		return genderDialog;
	}

	public EditText getGender() {
		return gender;
	}

	public void setGenderValue(String gender) {
		this.genderValue = gender;
	}

}
