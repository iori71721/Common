package com.iori.custom.common.application;

import android.app.Application;
import android.util.Log;

import com.iori.custom.common.app.ActivityLifeCycleHelper;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerAppLifeCycle();
    }

    private void registerAppLifeCycle(){
        ActivityLifeCycleHelper
                .init(this)
                .setAppLifeCycleListener(new ActivityLifeCycleHelper.AppLifeCycleListener() {
                    @Override
                    public void onEnterForeground() {
                    }

                    @Override
                    public void onEnterBackground() {
                    }
                });
    }
}
