package net.ipetty.android.petty;

import java.text.SimpleDateFormat;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.util.AppUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class PettyActivity extends Activity {

	public final static String TAG = PettyActivity.class.getSimpleName();

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Integer currUserId;

	private DisplayImageOptions options = AppUtils.getNormalImageOptions();

	private TextView title; // 标题
	private ImageView backButton; // 返回
	private TextView saveButton; // 保存

	private ImageView avatar; // 头像
	private EditText nickname; // 昵称

	private Dialog genderDialog; // 性别选择对话框
	private EditText gender; // 性别展现
	private String genderValue; // 性别传值

	private Dialog birthdayDialog; // 生日选择对话框
	private EditText birthday; // 生日

	private Dialog familyDialog; // 家族选择对话框
	private EditText family; // 家族

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_petty);

		// Title & Back
		backButton = (ImageView) this.findViewById(R.id.action_bar_left_image);
		backButton.setOnClickListener(new BackClickListener(this));
		title = (TextView) this.findViewById(R.id.action_bar_title);
		title.setText(this.getResources().getString(R.string.title_activity_petty));

		saveButton = (TextView) this.findViewById(R.id.save);

		avatar = (ImageView) this.findViewById(R.id.pet_avatar); // 头像
		nickname = (EditText) this.findViewById(R.id.pet_name); // 昵称
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.petty, menu);
		return true;
	}

}
