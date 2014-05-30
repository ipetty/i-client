package net.ipetty.android.login;

import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.ActivityUtils;
import net.ipetty.android.main.MainActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	public Handler mHandler=new Handler()  
    {  
        public void handleMessage(Message msg)  
        {  
        	//TODO:更新界面
            switch(msg.what)  
            {  
            case 1:  
                break;  
            default:  
                break;        
            }  
            super.handleMessage(msg);  
        }  
    };  
	
	public class LoginThread implements Runnable {
		
		@Override  
        public void run()  
        {  
			//TODO:登录过程
            Message message=new Message();  
            message.what=1;  
            
            
            mHandler.sendMessage(message);  
        }  
		
	}

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

			}
		});
		View qq = this.findViewById(R.id.qq);
		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO qq login

			}
		});

	}

	private final OnClickListener loginOnClick = new OnClickListener() {
		@Override
		public void onClick(View loginBtnView) {
			if (!validateLogin()) {
				return;
			}

			new LoginAsyncTask().execute();
		}
	};

	private boolean validateLogin() {
		this.account = accountView.getText().toString();
		this.password = passwordView.getText().toString();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// AsyncTask
	public class LoginAsyncTask extends AsyncTask<Integer, Integer, Integer> {
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			String str = getResources().getString(R.string.login_loading);
			this.progress = ProgressDialog.show(LoginActivity.this, null, str);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.progress.dismiss();
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			ActivityUtils.getInstance().finish();// 退出所有的Activity
		}

	}

}
