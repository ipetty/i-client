package net.ipetty.android.login;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;

public class LoginTask extends Task<String, UserVO> {

    public final static String TAG = LoginTask.class.getSimpleName();

    public LoginTask(Activity activity) {
        super(activity, new LoginTaskListener(activity));
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
