package net.ipetty.android.petty;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.sdk.task.foundation.GetOptionValueLabelMap;
import net.ipetty.android.sdk.task.foundation.ListOptions;
import net.ipetty.android.sdk.task.pet.GetPetById;
import net.ipetty.android.sdk.task.pet.UpdatePet;
import net.ipetty.android.sdk.task.user.UpdateUserAvatar;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.PetVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PettyActivity extends BaseActivity {

	private static final int REQUEST_CODE_PHOTORESOULT = 20;

	public final static String TAG = PettyActivity.class.getSimpleName();

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Integer currUserId;
	private Integer petId;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private TextView title; // 标题
	private ImageView backButton; // 返回
	private TextView saveButton; // 保存

	private ImageView avatar; // 头像
	private String mImageName = "cacheHead.jpg"; // 默认头像值
	private Dialog changeAvatarDialog; // 更换头像对话框

	private EditText nickname; // 昵称

	private Dialog genderDialog; // 性别选择对话框
	private EditText gender; // 性别展现
	private String genderValue; // 性别传值

	private Dialog birthdayDialog; // 生日选择对话框
	private EditText birthday; // 生日

	private Dialog familyDialog; // 家族选择对话框
	private EditText family; // 家族
	private String familyValue; // 家族传值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petty);

		// TODO init petId from Intent

		// Title & Back
		backButton = (ImageView) this.findViewById(R.id.action_bar_left_image);
		backButton.setOnClickListener(new BackClickListener(this));
		title = (TextView) this.findViewById(R.id.action_bar_title);
		title.setText(this.getResources().getString(R.string.title_activity_petty));

		// 保存
		saveButton = (TextView) this.findViewById(R.id.save);
		saveButton.setOnClickListener(saveClick);

		// 头像
		avatar = (ImageView) this.findViewById(R.id.pet_avatar); // 头像
		avatar.setOnClickListener(changeAvatarClick);

		// 昵称
		nickname = (EditText) this.findViewById(R.id.pet_name); // 昵称

		// 性别
		gender = (EditText) this.findViewById(R.id.pet_gender);
		new ListOptions(PettyActivity.this).setListener(initGenderDialog).execute(OptionGroup.PET_GENDER);

		// 生日
		birthday = (EditText) this.findViewById(R.id.pet_birthday);
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				birthdayDialog = DialogUtils.datePopupDialog(PettyActivity.this, selectBirthdayClick, birthday
						.getText().toString(), birthdayDialog);
			}
		});

		// 家族
		family = (EditText) this.findViewById(R.id.pet_family);
		new ListOptions(PettyActivity.this).setListener(initGenderFamily).execute(OptionGroup.PET_FAMILY);

		// 填充宠物信息
		// TODO 从缓存中获取宠物信息
		new GetPetById(PettyActivity.this).setListener(new DefaultTaskListener<PetVO>(PettyActivity.this) {
			@Override
			public void onSuccess(PetVO user) {
				// 头像
				if (StringUtils.isNotEmpty(user.getAvatar())) {
					Log.i(TAG, "设置用户头像：" + Constant.FILE_SERVER_BASE + user.getAvatar());
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar,
							options);
				}

				// 昵称
				PettyActivity.this.nickname.setText(user.getNickname() == null ? "" : user.getNickname());

				// 性别
				if (StringUtils.isNotBlank(user.getGender())) {
					PettyActivity.this.genderValue = user.getGender();
					new GetOptionValueLabelMap(PettyActivity.this).setListener(
							new DefaultTaskListener<Map<String, String>>(PettyActivity.this) {
								@Override
								public void onSuccess(Map<String, String> optionValueLabelMap) {
									String label = optionValueLabelMap.get(PettyActivity.this.genderValue);
									PettyActivity.this.gender.setText(label);
								}
							}).execute(OptionGroup.HUMAN_GENDER);
				}

				// 生日
				if (user.getBirthday() != null) {
					PettyActivity.this.birthday.setText(dateFormat.format(user.getBirthday()));
				}

				// 家族
				if (StringUtils.isNotBlank(user.getGender())) {
					PettyActivity.this.genderValue = user.getGender();
					new GetOptionValueLabelMap(PettyActivity.this).setListener(
							new DefaultTaskListener<Map<String, String>>(PettyActivity.this) {
								@Override
								public void onSuccess(Map<String, String> optionValueLabelMap) {
									String label = optionValueLabelMap.get(PettyActivity.this.genderValue);
									PettyActivity.this.gender.setText(label);
								}
							}).execute(OptionGroup.HUMAN_GENDER);
				}

			}
		}).execute(PettyActivity.this.petId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.petty, menu);
		return true;
	}

	/**
	 * 更改头像
	 */
	private OnClickListener changeAvatarClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			PettyActivity.this.showCameraDialog(view);
		}
	};

	/**
	 * 选择生日
	 */
	private OnDateSetListener selectBirthdayClick = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, monthOfYear, dayOfMonth);
			String str = dateFormat.format(calendar.getTime());
			birthday.setText(str);
		}
	};

	/**
	 * 保存
	 */
	private OnClickListener saveClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			PetVO pet = new PetVO();
			pet.setId(PettyActivity.this.petId);

			// 昵称
			String nickname = PettyActivity.this.nickname.getText().toString();
			pet.setNickname(nickname);
			if (StringUtils.isEmpty(nickname)) {
				PettyActivity.this.nickname.requestFocus();
				Toast.makeText(PettyActivity.this, R.string.nickname_empty, Toast.LENGTH_SHORT).show();
				return;
			}

			// 性别
			pet.setGender(PettyActivity.this.genderValue);

			// 生日
			String birthday = PettyActivity.this.birthday.getText().toString();
			if (StringUtils.isNotBlank(birthday)) {
				try {
					pet.setBirthday(dateFormat.parse(birthday));
				} catch (ParseException e) {
					Log.e(TAG, e.getMessage());
				}
			}

			// 家族
			pet.setFamily(PettyActivity.this.familyValue);

			new UpdatePet(PettyActivity.this).setListener(
					new DefaultTaskListener<PetVO>(PettyActivity.this, PettyActivity.this
							.getString(R.string.submitting)) {
						@Override
						public void onSuccess(PetVO result) {
							Toast.makeText(activity, R.string.save_success, Toast.LENGTH_SHORT).show();
							// TODO 更新缓存
						}
					}).execute(pet);
		}
	};

	public void showCameraDialog(View view) {
		OnClickListener[] Listener = new OnClickListener[] { takePhotoClick, pickPhotoClick };
		this.changeAvatarDialog = DialogUtils.bottomPopupDialog(this, Listener, R.array.alert_camera,
				getString(R.string.camera_title), this.changeAvatarDialog);
	}

	private final OnClickListener takePhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.takePicture(PettyActivity.this, PathUtils.getCarmerDir(), PettyActivity.this.mImageName);
			PettyActivity.this.changeAvatarDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.chooserSysPics(PettyActivity.this);
			PettyActivity.this.changeAvatarDialog.cancel();
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
			this.updateAvatar(picture.getAbsolutePath());
		}
	}

	public void updateAvatar(final String filePath) {
		// TODO
		new UpdateUserAvatar(this).setListener(new DefaultTaskListener<String>(this) {
			@Override
			public void onSuccess(String result) {
				Log.i(TAG, "updateAvatar.onSuccess:" + result);
				UserApiWithCache.removeCache(currUserId);
				UserVO user = UserApiWithCache.getUserById4Synchronous(PettyActivity.this, currUserId);
				ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar, options);
				PettyActivity.this.showMessageForLongTime("更新头像成功");
			}

			@Override
			public void onError(Throwable ex) {
				super.onError(ex);
				PettyActivity.this.showMessageForLongTime("更新头像失败");
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
		super.startActivityForResult(intent, REQUEST_CODE_PHOTORESOULT);
	}

	/**
	 * 初始化性别选择对话框
	 */
	private DefaultTaskListener<List<Option>> initGenderDialog = new DefaultTaskListener<List<Option>>(
			PettyActivity.this) {
		private List<ModDialogItem> dialogItems;

		@Override
		public void onSuccess(List<Option> options) {
			dialogItems = new ArrayList<ModDialogItem>();
			for (Option option : options) {
				dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
			}

			PettyActivity.this.gender.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					PettyActivity.this.genderDialog = DialogUtils.modPopupDialog(PettyActivity.this, dialogItems,
							PettyActivity.this.genderDialog);
				}
			});
		}

		private OnClickListener dialogClick = new OnClickListener() {
			@Override
			public void onClick(View view) {
				String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
				String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
				PettyActivity.this.gender.setText(label);
				PettyActivity.this.genderValue = value;
				PettyActivity.this.genderDialog.cancel();
			}
		};
	};

	/**
	 * 初始化家族选择对话框
	 */
	private DefaultTaskListener<List<Option>> initGenderFamily = new DefaultTaskListener<List<Option>>(
			PettyActivity.this) {
		private List<ModDialogItem> dialogItems;

		@Override
		public void onSuccess(List<Option> options) {
			dialogItems = new ArrayList<ModDialogItem>();
			for (Option option : options) {
				dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
			}

			PettyActivity.this.family.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					PettyActivity.this.familyDialog = DialogUtils.modPopupDialog(PettyActivity.this, dialogItems,
							PettyActivity.this.familyDialog);
				}
			});
		}

		private OnClickListener dialogClick = new OnClickListener() {
			@Override
			public void onClick(View view) {
				String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
				String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
				PettyActivity.this.family.setText(label);
				PettyActivity.this.familyValue = value;
				PettyActivity.this.familyDialog.cancel();
			}
		};
	};

}
