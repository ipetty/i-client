package net.ipetty.android.login;

import android.util.Log;
import net.ipetty.android.core.MyAsyncTask;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.core.util.ActivityUtils;
import net.ipetty.android.core.util.AppUtils;
import net.ipetty.android.main.MainActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;

public class RegisterTask extends MyAsyncTask<RegisterVO, UserVO> {

    public final static String TAG = "LoginTask";

    public RegisterTask(BaseActivity activity) {
        super(activity);
    }

    @Override
    protected UserVO myDoInBackground(RegisterVO... args) {
        //TODO
        RegisterVO rvo = args[0];

        IpetApi api = IpetApi.init(activity);
        return api.getUserApi().register(rvo);
    }

    @Override
    protected void onPostExecute(UserVO user) {
        super.onPostExecute(user);
        this.goMain();
    }

    // 转向主界面
    public void goMain() {
        Log.i(TAG, "to MainActivity");
        AppUtils.goTo(activity, MainActivity.class);
        ActivityUtils.getInstance().finish();
    }

}
