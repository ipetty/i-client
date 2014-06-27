package net.ipetty.android.sdk.task.pet;

import java.util.List;

import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;
import net.ipetty.vo.PetVO;
import android.app.Activity;

public class ListPetsByUserId extends Task<Integer, List<PetVO>> {

	public ListPetsByUserId(Activity activity) {
		super(activity);
	}

	@Override
	protected List<PetVO> myDoInBackground(Integer... args) {
		return IpetApi.init(activity).getPetApi().listByUserId(args[0]);
	}

}
