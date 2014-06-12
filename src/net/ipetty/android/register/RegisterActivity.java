package net.ipetty.android.register;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import net.ipetty.R;
import net.ipetty.android.core.ui.BackClickListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.ui.ModDialogItem;
import net.ipetty.android.core.util.DialogUtils;
import net.ipetty.vo.RegisterVO;

public class RegisterActivity extends BaseActivity {

    private Dialog sexDialog;
    private Dialog typeDialog;
    private EditText accountView;
    private EditText passwordView;
    private TextView btnSexy;
    private TextView btnType;
    private TextView toggleView = null;
    private boolean psdDisplayFlg = false;
    private List<ModDialogItem> sexyItems;
    private List<ModDialogItem> typeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* action bar */
        ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
        TextView text = (TextView) this.findViewById(R.id.action_bar_title);
        text.setText(this.getResources().getString(R.string.title_activity_register));
        btnBack.setOnClickListener(new BackClickListener(this));

        //注册
        View registerBtnView = (View) this.findViewById(R.id.button);
        registerBtnView.setOnClickListener(registerOnClick);

        accountView = (EditText) this.findViewById(R.id.account);
        passwordView = (EditText) this.findViewById(R.id.password);

        toggleView = (TextView) this.findViewById(R.id.login_toggle_password);
        toggleView.setOnClickListener(togglePasswordClick);

        sexyItems = new ArrayList<ModDialogItem>();
        sexyItems.add(new ModDialogItem(null, "男生", sexOnClick));
        sexyItems.add(new ModDialogItem(null, "女生", sexOnClick));
        sexyItems.add(new ModDialogItem(null, "男女生", sexOnClick));

        btnSexy = (TextView) this.findViewById(R.id.sex);
        btnSexy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sexDialog = DialogUtils.modPopupDialog(RegisterActivity.this, sexyItems, sexDialog);
            }
        });

        typeItems = new ArrayList<ModDialogItem>();
        typeItems.add(new ModDialogItem(null, "汪星人", typeOnClick));
        typeItems.add(new ModDialogItem(null, "喵星人", typeOnClick));
        typeItems.add(new ModDialogItem(null, "水星人", typeOnClick));
        typeItems.add(new ModDialogItem(null, "冷星人", typeOnClick));
        typeItems.add(new ModDialogItem(null, "异星人", typeOnClick));

        btnType = (TextView) this.findViewById(R.id.type);
        btnType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                typeDialog = DialogUtils.modPopupDialog(RegisterActivity.this, typeItems, typeDialog);
            }
        });

    }

    private RegisterVO getRegisterVO() {
        //TODO:
        String email = accountView.getText().toString();
        String pass = passwordView.getText().toString();
        //String sexy = btnSexy.get;
        //String sexy = btnType.get;
        RegisterVO rvo = new RegisterVO();

        rvo.setPetFamily(email);

        return rvo;
    }

    // 登录
    private final OnClickListener registerOnClick = new OnClickListener() {
        @Override
        public void onClick(View registerBtnView) {
            RegisterVO rvo = getRegisterVO();
            if (!validate(rvo)) {
                return;
            }

            new RegisterTask(RegisterActivity.this).execute(rvo);
        }
    };

    private boolean validate(RegisterVO rvo) {
        //TODO:
        return true;
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

    private OnClickListener sexOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
            btnSexy.setText(text);
            sexDialog.cancel();
        }
    };

    private OnClickListener typeOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
            btnType.setText(text);
            typeDialog.cancel();
        }
    };
    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
     * menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.register, menu); return true; }
     */

}
