package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.ValidUtils;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	public final static String TAG = "LoginActivity";
	private EditText accountView;
	private EditText passwordView;
	private String account = null;
	private String password = null;
	private int focuscont = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_login));
		btnBack.setOnClickListener(new BackClickListener(this));

		// 注册
		TextView btnReg = (TextView) this.findViewById(R.id.register);
		btnReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		// 忘记密码
		TextView forgotView = (TextView) this.findViewById(R.id.forget_password);
		forgotView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "暂时未实现", Toast.LENGTH_SHORT).show();
			}
		});

		accountView = (EditText) this.findViewById(R.id.account);
		accountView.setOnFocusChangeListener(new OnFocusChangeListener(){

            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if(hasFocus){
                	accountView.setHint(null);
                	if(focuscont==0){
                		accountView.clearFocus();
                	}
                	focuscont++;
                }else{
                	accountView.setHint("Email");
                }
                
            }
            
        });
		passwordView = (EditText) this.findViewById(R.id.password);
		// 登陆
		View loginBtnView = (View) this.findViewById(R.id.button);
		loginBtnView.setOnClickListener(loginOnClick);

		// sina
		View sina = this.findViewById(R.id.sina);
		sina.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO sina Login
				Toast.makeText(LoginActivity.this, "暂时未实现", Toast.LENGTH_SHORT).show();

			}
		});
		View qq = this.findViewById(R.id.qq);
		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO qq login
				Toast.makeText(LoginActivity.this, "暂时未实现", Toast.LENGTH_SHORT).show();

			}
		});

	}

	//登录
	private final OnClickListener loginOnClick = new OnClickListener() {
		@Override
		public void onClick(View loginBtnView) {
			if (!validateLogin()) {
				return;
			}
			new LoginTask(LoginActivity.this).execute(account,password);
		}
	};
	
	//登录前校验
	private boolean validateLogin() {
		this.account = accountView.getText().toString();
		this.password = passwordView.getText().toString();
		
		if(StringUtils.isBlank(this.account)){
			accountView.requestFocus();
			Toast.makeText(this, "Email不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!ValidUtils.isEmail(this.account)){
			accountView.requestFocus();
			Toast.makeText(this, "Email格式不正确",Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		if(StringUtils.isBlank(this.password)){
			passwordView.requestFocus();
			Toast.makeText(this, "密码不能为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	

}
