package net.ipetty.android.register;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.ipetty.R;
import net.ipetty.android.api.UserApiWithCache;
import net.ipetty.android.core.ActivityManager;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.login.LoginActivity;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.core.SDKStateManager;
import net.ipetty.android.sdk.task.foundation.GetOptionValueLabelMap;
import net.ipetty.android.sdk.task.foundation.ListOptions;
import net.ipetty.android.sdk.task.foundation.SetOptionLabelTaskListener;
import net.ipetty.android.sdk.task.pet.ListPetsByUserId;
import net.ipetty.android.sdk.task.user.ImproveUserInfo43rd;
import net.ipetty.android.sdk.task.user.UpdateUserAvatar;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.PetVO;
import net.ipetty.vo.UserForm43rdVO;
import net.ipetty.vo.UserVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class Register3rdActivity extends BaseActivity {

	private Integer currUserId;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private TextView title; // 标题
	private ImageView backButton; // 返回

	private AutoCompleteTextView emailEditor;
	private EditText nicknameEditor;

	private Integer petId;

	private EditText petNameEditor;

	private Dialog petGenderDialog;
	private TextView petGenderText;
	private String petGender;

	private Dialog petFamilyDialog;
	private TextView petFamilyText;
	private String petFamily;

	private Button submitButton;

	private static final int REQUEST_CODE_PHOTORESOULT = 20;
	private ImageView avatar; // 头像
	private Dialog changeAvatarDialog; // 更换头像对话框
	private String mImageName = Constant.PIC_USER_HEAD_IMAGE_NAME; // 默认头像值

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3rd);

		currUserId = IpetApi.init(this).getCurrUserId();

		// Title & Back
		title = (TextView) this.findViewById(R.id.action_bar_title);
		title.setText(this.getResources().getString(R.string.title_activity_register3rd));
		backButton = (ImageView) this.findViewById(R.id.action_bar_left_image);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppUtils.goTo(Register3rdActivity.this, LoginActivity.class);
				Register3rdActivity.this.finish();
			}
		});

		emailEditor = (AutoCompleteTextView) this.findViewById(R.id.account);
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(this).getAccounts();
		final List<String> emailList = new ArrayList<String>();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				emailList.add(account.name);
			}
		}
		String emails[] = new String[emailList.size()];
		if (emailList.size() > 0) {
			for (int i = 0, len = emailList.size(); i < len; i++) {
				emails[i] = emailList.get(i);
			}
		}

		// 自动提示
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emails);
		emailEditor.setAdapter(adapt);

		emailEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {// 如果组件失去焦点
					String emailstr = emailEditor.getText().toString();
					String nickName = nicknameEditor.getText().toString();
					if (StringUtils.isBlank(nickName) && emailstr.contains("@")) {
						nicknameEditor.setText(emailstr.split("@")[0]);
					}
				} else {
					if (emailList.size() > 0) {
						emailEditor.showDropDown();
					}
				}
			}
		});

		nicknameEditor = (EditText) this.findViewById(R.id.nickname);
		nicknameEditor.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				petNameEditor.setText(s + "的爱宠");
			}

			public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
			}

			public void afterTextChanged(Editable edtbl) {
			}
		});

		avatar = (ImageView) this.findViewById(R.id.avatar); // 头像
		avatar.setOnClickListener(changeAvatarClick);

		petNameEditor = (EditText) this.findViewById(R.id.pet_name);

		petGenderText = (TextView) this.findViewById(R.id.pet_gender);
		new ListOptions(this).setListener(initGenderDialog).execute(OptionGroup.PET_GENDER);
		petFamilyText = (TextView) this.findViewById(R.id.pet_family);
		new ListOptions(this).setListener(initGenderFamily).execute(OptionGroup.PET_FAMILY);

		submitButton = (Button) this.findViewById(R.id.button);
		submitButton.setOnClickListener(sumbit);

		// Toast.makeText(Register3rdActivity.this, "请完善个人资料",
		// Toast.LENGTH_SHORT).show();

		// 填充用户信息
		Register3rdActivity.this.fullfillUserInfo();

		// 填充宠物信息
		Register3rdActivity.this.fullfillPetInfo();
	}

	// 填充用户信息
	private void fullfillUserInfo() {
		UserApiWithCache.getUserById4Asynchronous(this, currUserId, new DefaultTaskListener<UserVO>(
				Register3rdActivity.this) {
			@Override
			public void onSuccess(UserVO user) {
				// 头像
				if (StringUtils.isNotEmpty(user.getAvatar())) {
					Log.d(TAG, "设置用户头像：" + Constant.FILE_SERVER_BASE + user.getAvatar());
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar,
							options);
				} else {
					String platformName = SDKStateManager.getPlatformName(activity);
					Platform platform = ShareSDK.getPlatform(Register3rdActivity.this, platformName);
					String userIcon = platform.getDb().getUserIcon();
					if (StringUtils.isNotBlank(userIcon)) {
						// 从第三方平台获取用户头像，保存后展现
						ImageLoader.getInstance().displayImage(userIcon, avatar, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri, View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								String path = PathUtils.getCarmerDir() + mImageName;
								File file = new File(path);
								FileOutputStream out;
								try {
									out = new FileOutputStream(file);
									loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
									Register3rdActivity.this.updateAvatar(file.getAbsolutePath(), null);
								} catch (FileNotFoundException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onLoadingCancelled(String imageUri, View view) {
							}
						});

					} else {
						avatar.setImageResource(R.drawable.avatar);
					}
				}

				// 邮箱地址
				Register3rdActivity.this.emailEditor.setText(user.getEmail() == null ? "" : user.getEmail());

				// 昵称
				Register3rdActivity.this.nicknameEditor.setText(user.getNickname() == null ? "" : user.getNickname());
			}
		});
	}

	// 填充宠物信息
	private void fullfillPetInfo() {
		Log.d(TAG, "loadPetsData");
		new ListPetsByUserId(Register3rdActivity.this).setListener(
				new DefaultTaskListener<List<PetVO>>(Register3rdActivity.this) {
					@Override
					public void onSuccess(List<PetVO> pets) {
						if (CollectionUtils.isEmpty(pets)) {
							// 无宠物则无需填充
							return;
						}

						// 此时不应该会出现多个宠物
						final PetVO pet = pets.get(0);
						Register3rdActivity.this.petId = pet.getId();

						// 昵称
						Register3rdActivity.this.petNameEditor.setText(pet.getNickname() == null ? "" : pet
								.getNickname());

						// 性别
						if (StringUtils.isNotBlank(pet.getGender())) {
							Register3rdActivity.this.petGender = pet.getGender();
							new GetOptionValueLabelMap(Register3rdActivity.this)
									.setListener(
											new SetOptionLabelTaskListener(Register3rdActivity.this,
													Register3rdActivity.this.petGenderText,
													Register3rdActivity.this.petGender))
									.execute(OptionGroup.PET_GENDER);
						}

						// 家族
						if (StringUtils.isNotBlank(pet.getFamily())) {
							Register3rdActivity.this.petFamily = pet.getFamily();
							new GetOptionValueLabelMap(Register3rdActivity.this)
									.setListener(
											new SetOptionLabelTaskListener(Register3rdActivity.this,
													Register3rdActivity.this.petFamilyText,
													Register3rdActivity.this.petFamily))
									.execute(OptionGroup.PET_FAMILY);
						}
					}
				}).execute(Register3rdActivity.this.currUserId);
	}

	private final OnClickListener sumbit = new OnClickListener() {
		@Override
		public void onClick(View view) {
			UserForm43rdVO userForm = new UserForm43rdVO();
			userForm.setId(Register3rdActivity.this.currUserId);
			userForm.setPetId(Register3rdActivity.this.petId);
			AppUtils.goTo(Register3rdActivity.this, MainActivity.class);
			Register3rdActivity.this.finish();
			/*
			 * // email String email = emailEditor.getText().toString();
			 * userForm.setEmail(email); if (StringUtils.isEmpty(email)) {
			 * emailEditor.requestFocus();
			 * Toast.makeText(Register3rdActivity.this,
			 * R.string.login_empty_account, Toast.LENGTH_SHORT).show(); return;
			 * } if (!ValidUtils.isEmail(email)) { emailEditor.requestFocus();
			 * Toast.makeText(Register3rdActivity.this,
			 * R.string.login_error_invalid_email, Toast.LENGTH_SHORT).show();
			 * return; }
			 */

			// nickname
			String nickname = nicknameEditor.getText().toString();
			userForm.setNickname(nickname);

			// pet name
			String petName = petNameEditor.getText().toString();
			userForm.setPetName(petName);

			// pet gender
			userForm.setPetGender(petGender);

			// pet family
			userForm.setPetFamily(petFamily);

			// 保存
			new ImproveUserInfo43rd(Register3rdActivity.this).setListener(
					new DefaultTaskListener<UserVO>(Register3rdActivity.this) {
						@Override
						public void onSuccess(UserVO result) {
							Toast.makeText(activity, R.string.save_success, Toast.LENGTH_SHORT).show();

							// 完善资料后跳转到首页
							AppUtils.goTo(activity, MainActivity.class);
							activity.finish();
						}
					}).execute(userForm);
		}
	};

	/**
	 * 初始化性别选择对话框
	 */
	private DefaultTaskListener<List<Option>> initGenderDialog = new DefaultTaskListener<List<Option>>(
			Register3rdActivity.this) {
		private List<ModDialogItem> dialogItems;

		@Override
		public void onSuccess(List<Option> options) {
			dialogItems = new ArrayList<ModDialogItem>();
			for (Option option : options) {
				dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
			}

			Register3rdActivity.this.petGenderText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Register3rdActivity.this.petGenderDialog = DialogUtils.modPopupDialog(Register3rdActivity.this,
							dialogItems, Register3rdActivity.this.petGenderDialog);
				}
			});
		}

		private OnClickListener dialogClick = new OnClickListener() {
			@Override
			public void onClick(View view) {
				String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
				String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
				Register3rdActivity.this.petGenderText.setText(label);
				Register3rdActivity.this.petGender = value;
				Register3rdActivity.this.petGenderDialog.cancel();
			}
		};
	};

	/**
	 * 初始化家族选择对话框
	 */
	private DefaultTaskListener<List<Option>> initGenderFamily = new DefaultTaskListener<List<Option>>(
			Register3rdActivity.this) {
		private List<ModDialogItem> dialogItems;

		@Override
		public void onSuccess(List<Option> options) {
			dialogItems = new ArrayList<ModDialogItem>();
			for (Option option : options) {
				dialogItems.add(new ModDialogItem(null, option.getValue(), option.getLabel(), dialogClick));
			}

			Register3rdActivity.this.petFamilyText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Register3rdActivity.this.petFamilyDialog = DialogUtils.modPopupDialog(Register3rdActivity.this,
							dialogItems, Register3rdActivity.this.petFamilyDialog);
				}
			});
		}

		private OnClickListener dialogClick = new OnClickListener() {
			@Override
			public void onClick(View view) {
				String label = ((TextView) view.findViewById(R.id.text)).getText().toString();
				String value = ((TextView) view.findViewById(R.id.value)).getText().toString();
				Register3rdActivity.this.petFamilyText.setText(label);
				Register3rdActivity.this.petFamily = value;
				Register3rdActivity.this.petFamilyDialog.cancel();
			}
		};
	};

	private OnClickListener changeAvatarClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Register3rdActivity.this.showCameraDialog(view);
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
			DeviceUtils.takePicture(Register3rdActivity.this, PathUtils.getCarmerDir(),
					Register3rdActivity.this.mImageName);
			Register3rdActivity.this.changeAvatarDialog.cancel();
		}
	};

	private final OnClickListener pickPhotoClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			DeviceUtils.chooserSysPics(Register3rdActivity.this);
			Register3rdActivity.this.changeAvatarDialog.cancel();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "finish");
		Log.d(TAG, "requestCode" + requestCode);
		Log.d(TAG, "resultCode" + resultCode);

		String path = PathUtils.getCarmerDir() + this.mImageName;
		File picture = new File(path);
		Uri pathUri = Uri.fromFile(picture);

		if (requestCode == DeviceUtils.REQUEST_CODE_PICK_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Uri uri = data.getData();
				Log.d(TAG, "finish" + uri);
				startPhotoZoom(uri, pathUri);
			}
		}
		if (requestCode == DeviceUtils.REQUEST_CODE_TAKE_IMAGE) {
			if (resultCode == FragmentActivity.RESULT_OK) {
				Log.d(TAG, "finish" + picture);
				startPhotoZoom(Uri.fromFile(picture), pathUri);
			}

		}
		if (requestCode == REQUEST_CODE_PHOTORESOULT) {
			Log.d(TAG, "crop" + pathUri);

			avatar.setImageURI(pathUri);
			this.updateAvatar(picture.getAbsolutePath(), "更新头像成功");
		}
	}

	public void updateAvatar(final String filePath, final String successMsg) {
		new UpdateUserAvatar(this).setListener(new DefaultTaskListener<String>(this) {
			@Override
			public void onSuccess(String result) {
				Log.d(TAG, "updateAvatar.onSuccess:" + result);
				UserVO user = UserApiWithCache.getUserById4Synchronous(Register3rdActivity.this, currUserId);
				if (StringUtils.isNotBlank(user.getAvatar())) {
					ImageLoader.getInstance().displayImage(Constant.FILE_SERVER_BASE + user.getAvatar(), avatar,
							options);
				} else {
					avatar.setImageResource(R.drawable.avatar);
				}
				if (StringUtils.isNotBlank(successMsg)) {
					Register3rdActivity.this.showMessageForLongTime(successMsg);
				}
			}

			@Override
			public void onError(Throwable ex) {
				super.onError(ex);
				Register3rdActivity.this.showMessageForLongTime("更新头像失败");
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
		intent.putExtra("outputX", Constant.ZOOM_IMAGE_MAX_WIDTH);
		intent.putExtra("outputY", Constant.ZOOM_IMAGE_MAX_HEIGHT);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("return-data", false);
		super.startActivityForResult(intent, REQUEST_CODE_PHOTORESOULT);
	}

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 双击返回，退出应用
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				String exit_once_again = getResources().getString(R.string.exit_once_again);
				Toast.makeText(this, exit_once_again, Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				ActivityManager.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
