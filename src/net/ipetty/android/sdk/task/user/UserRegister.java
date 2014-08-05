package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.RegisterVO;
import net.ipetty.vo.UserVO;

public class UserRegister extends Task<RegisterVO, UserVO> {

	public UserRegister(BaseActivity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(RegisterVO... args) {
		return IpetApi.init(activity).getUserApi().register(args[0]);
	}

}
