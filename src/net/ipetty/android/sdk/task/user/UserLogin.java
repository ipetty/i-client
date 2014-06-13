package net.ipetty.android.sdk.task.user;

import android.app.Activity;
import android.util.Log;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserVO;

public class UserLogin extends Task<String, UserVO> {

    public final static String TAG = UserLogin.class.getSimpleName();

    public UserLogin(Activity activity) {
        super(activity);
    }

    @Override
    protected UserVO myDoInBackground(String... args) {
        Log.d(TAG, "myDoInBackground");
        String loginName = args[0];
        String password = args[1];

        return IpetApi.
                init(activity).
                getUserApi().
                login(loginName, password);
    }
}
