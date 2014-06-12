package net.ipetty.android.login;

import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;

public class RegisterTask extends Task<RegisterVO, UserVO> {

    public final static String TAG = "LoginTask";

    public RegisterTask(BaseActivity activity) {
        super(activity, new RegisterTaskListener(activity));
    }

    @Override
    protected UserVO myDoInBackground(RegisterVO... args) {
        RegisterVO rvo = args[0];
        IpetApi api = IpetApi.init(activity);
        return api.getUserApi().register(rvo);
    }

}
