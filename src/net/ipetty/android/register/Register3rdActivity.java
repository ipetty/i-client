package net.ipetty.android.register;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.ipetty.R;
import net.ipetty.android.core.Constant;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DeviceUtils;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.android.core.util.PathUtils;
import net.ipetty.android.core.util.ValidUtils;
import net.ipetty.android.sdk.task.foundation.ListOptions;
import net.ipetty.vo.Option;
import net.ipetty.vo.OptionGroup;
import net.ipetty.vo.RegisterVO;

import org.apache.commons.lang3.StringUtils;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register3rdActivity extends BaseActivity {
	private String TAG = Register3rdActivity.class.getSimpleName();

	private AutoCompleteTextView emailEditor;
	private EditText nicknameEditor;
	private EditText petNameEditor;
	private Dialog petGenderDialog;
	private List<ModDialogItem> petGenderItems = new ArrayList<ModDialogItem>();
	private TextView petGenderText;
	private String petGender;
	private Dialog petFamilyDialog;
	private List<ModDialogItem> petFamilyItems = new ArrayList<ModDialogItem>();
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

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register3rd));
		btnBack.setOnClickListener(new BackClickListener(this));

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
	}

	private final OnClickListener sumbit = new OnClickListener() {
		@Override
		public void onClick(View view) {
			RegisterVO register = new RegisterVO();

			// email
			String email = emailEditor.getText().toString();
			register.setEmail(email);
			if (StringUtils.isEmpty(email)) {
				emailEditor.requestFocus();
				Toast.makeText(Register3rdActivity.this, R.string.login_empty_account, Toast.LENGTH_SHORT).show();
				return;
			}
			if (!ValidUtils.isEmail(email)) {
				emailEditor.requestFocus();
				Toast.makeText(Register3rdActivity.this, R.string.login_error_invalid_email, Toast.LENGTH_SHORT).show();
				return;
			}

			// nickname
			String nickname = nicknameEditor.getText().toString();
			register.setNickname(nickname);

			// TODO gender
			// pet name
			String petName = petNameEditor.getText().toString();
			register.setPetName(petName);

			// pet gender
			register.setPetGender(petGender);

			// pet family
			register.setPetFamily(petFamily);

			// new UserRegister(Register3rdActivity.this).setListener(
			// new RegisterTaskListener(Register3rdActivity.this,
			// register)).execute(register);
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
			// TODO:
			// this.updateAvatar(picture.getAbsolutePath());
		}
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

}
