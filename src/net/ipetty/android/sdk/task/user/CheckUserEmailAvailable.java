package net.ipetty.android.sdk.task.user;

import android.app.Activity;
import net.ipetty.android.core.Task;
import net.ipetty.android.sdk.core.IpetApi;

public class CheckUserEmailAvailable extends Task<String, Boolean> {

    public final static String TAG = CheckUserEmailAvailable.class.getSimpleName();

    public CheckUserEmailAvailable(Activity activity) {
        super(activity);
    }

    @Override
    protected Boolean myDoInBackground(String... args) {
        return IpetApi.init(activity).getUserApi().checkEmailAvailable(args[0]);
    }

}
