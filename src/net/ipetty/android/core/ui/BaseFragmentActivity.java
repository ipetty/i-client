package net.ipetty.android.core.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import net.ipetty.android.core.ActivityManager;

public class BaseFragmentActivity extends FragmentActivity {

    public void showMessageForShortTime(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showMessageForLongTime(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
