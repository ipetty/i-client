package net.ipetty.android.sdk.task.pet;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.PetVO;
import android.app.Activity;

public class GetPetById extends Task<Integer, PetVO> {

	public GetPetById(Activity activity) {
		super(activity);
	}

	@Override
	protected PetVO myDoInBackground(Integer... args) {
		return IpetApi.init(activity).getPetApi().getById(args[0]);
	}

}
