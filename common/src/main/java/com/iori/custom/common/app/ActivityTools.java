package com.iori.custom.common.app;

import android.app.Activity;
import android.util.Log;

public class ActivityTools {
    public static void runOnUiThreadWhenActivityIsValid(final Activity activity, final Runnable executeRunnable){
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if(!activity.isFinishing()){
                            activity.runOnUiThread(executeRunnable);
                        }
                    }
                }
        );
    }
}
