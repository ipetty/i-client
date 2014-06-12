package net.ipetty.android.boot;

import net.ipetty.android.core.Task;
import net.ipetty.android.core.ui.BaseActivity;

public class SplashTask extends Task<Void, Void> {

    public final static String TAG = "SplashTask";

    public SplashTask(BaseActivity activity) {
        super(activity, new SplashTaskListener(activity));
    }

    //重写onPreExecute防止出现loading...
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void myDoInBackground(Void... args) {
        try {
            //延时让用户看到启动画面
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        return null;
    }

}
