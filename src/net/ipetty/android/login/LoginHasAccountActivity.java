package net.ipetty.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.ipetty.R;
import net.ipetty.android.core.DefaultTaskListener;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.android.sdk.task.user.GetUserById;
import net.ipetty.android.sdk.task.user.UserLogin;
import net.ipetty.vo.UserVO;
import org.apache.commons.lang3.StringUtils;

public class LoginHasAccountActivity extends BaseActivity {

    DisplayImageOptions options;

    private EditText passwordView;
    private TextView toggleView = null;
    private boolean psdDisplayFlg = false;
    private TextView account = null;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_has_account);

        /* action bar */
        ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
        TextView text = (TextView) this.findViewById(R.id.action_bar_title);
        text.setText(this.getResources().getString(R.string.title_activity_login_has_account));
        btnBack.setOnClickListener(new BackClickListener(this));

        TextView changeAccount = (TextView) this.findViewById(R.id.changeAccount);
        changeAccount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginHasAccountActivity.this, LoginActivity.class);
                LoginHasAccountActivity.this.startActivity(intent);
            }
        });

        options = AppUtils.getRoundedImageOptions();
        account = (TextView) this.findViewById(R.id.account);

        // avator
        avatar = (ImageView) this.findViewById(R.id.avatar);

        // passworid
        passwordView = (EditText) this.findViewById(R.id.password);
        toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
        toggleView.setOnClickListener(togglePasswordClick);
        // login btn
        Button loginBtn = (Button) this.findViewById(R.id.button);
        loginBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new UserLogin(LoginHasAccountActivity.this)
                        .setListener(new LoginTaskListener(LoginHasAccountActivity.this))
                        .execute(account.getText().toString(), passwordView.getText().toString());
            }
        });
        loadData();
    }

    private void loadData() {
        IpetApi api = IpetApi.init(this);
        new GetUserById(this)
                .setListener(new DefaultTaskListener<UserVO>(this) {
                    @Override
                    public void onSuccess(UserVO result) {
                        if (!StringUtils.isEmpty(result.getEmail())) {
                            account.setText(result.getEmail());
                        }
                        if (!StringUtils.isEmpty(result.getAvatar())) {
                            ImageLoader.getInstance().displayImage(result.getAvatar(), avatar, options);
                        }

                    }
                })
                .execute(api.getCurrUserId());
    }

    // 密码可见
    private OnClickListener togglePasswordClick = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            int index = passwordView.getSelectionStart();
            if (!psdDisplayFlg) {
                // display password text, for example "123456"
                // passwordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleView.setText(R.string.login_toggle_password_hide);
            } else {
                // hide password, display "."
                // passwordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleView.setText(R.string.login_toggle_password_show);
            }
            psdDisplayFlg = !psdDisplayFlg;
            Editable etable = passwordView.getText();
            Selection.setSelection(etable, index);
        }
    };

}
