package net.ipetty.android.sdk.task.user;

import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.UserForm43rdVO;
import net.ipetty.vo.UserVO;

/**
 * ImproveUserInfo43rd
 * 
 * @author luocanfeng
 * @date 2014年8月5日
 */
public class ImproveUserInfo43rd extends Task<UserForm43rdVO, UserVO> {

	public ImproveUserInfo43rd(BaseActivity activity) {
		super(activity);
	}

	@Override
	protected UserVO myDoInBackground(UserForm43rdVO... args) {
		return IpetApi.init(activity).getUserApi().improveUserInfo43rd(args[0]);
	}

}
