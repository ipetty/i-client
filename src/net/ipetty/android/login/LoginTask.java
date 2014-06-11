package net.ipetty.android.login;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.Task;
import net.ipetty.android.core.TaskListener;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.ActivityUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;

public class LoginTask extends Task<String, UserVO> {

    public final static String TAG = LoginTask.class.getSimpleName();

    public LoginTask(Activity activity, TaskListener<UserVO> listener) {
        super(activity, listener);
    }

    @Override
    protected UserVO myDoInBackground(String... args) {
        Log.d(TAG, "myDoInBackground");
        String loginName = args[0];
        String password = args[1];

        IpetApi api = IpetApi.init(activity);
        return api.getUserApi().login(loginName, password);
    }
}
