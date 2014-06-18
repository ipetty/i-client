package net.ipetty.android.core.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import net.ipetty.android.core.ActivityManager;

public class BaseActivity extends Activity {

    private ProgressDialog progressDialog;

    //private boolean destroyed = false;
    public void showMessageForShortTime(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showMessageForLongTime(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //显示异步加载提示对话框
    public void showProgressDialog(CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
        }

        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    //销毁异步加载提示对话框
    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getInstance().distoryActivity(this);
        super.onDestroy();
    }

}
