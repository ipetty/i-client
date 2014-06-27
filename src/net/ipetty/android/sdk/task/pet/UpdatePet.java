package net.ipetty.android.sdk.task.pet;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.PetVO;
import android.app.Activity;

/**
 * UpdatePet
 * 
 * @author luocanfeng
 * @date 2014年6月27日
 */
public class UpdatePet extends Task<PetVO, PetVO> {

	public UpdatePet(Activity activity) {
		super(activity);
	}

	@Override
	protected PetVO myDoInBackground(PetVO... args) {
		return IpetApi.init(activity).getPetApi().update(args[0]);
	}

}
