package net.ipetty.android.boot;

import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;
import net.ipetty.android.update.UpdateUtils;

public class CheckUpdateTask extends Task<Void, Boolean> {

	public CheckUpdateTask(BaseActivity activity) {
		super(activity);
	}

	@Override
	protected Boolean myDoInBackground(Void... args) {
		return UpdateUtils.hasUpdate(activity);
	}
}
