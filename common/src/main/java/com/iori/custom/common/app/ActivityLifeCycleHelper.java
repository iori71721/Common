package com.iori.custom.common.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ref https://gist.github.com/Yuniks/37f5d106281350beb86703bea32c6bc2
 */
public class ActivityLifeCycleHelper implements Application.ActivityLifecycleCallbacks {

    private static ActivityLifeCycleHelper instance;

    private int numStarted = 0;
    private AppLifeCycleListener appLifeCycleListener;

    public static ActivityLifeCycleHelper init(Application application) {
        if (instance == null) {
            instance = new ActivityLifeCycleHelper();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public void setAppLifeCycleListener(AppLifeCycleListener appLifeCycleListener) {
        this.appLifeCycleListener = appLifeCycleListener;
    }

    public ActivityLifeCycleHelper() {
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            if (appLifeCycleListener != null) {
                appLifeCycleListener.onEnterForeground();
            }
        }
        numStarted++;
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            if(appLifeCycleListener != null){
                appLifeCycleListener.onEnterBackground();
            }
        }
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {

    }

    public interface AppLifeCycleListener {
        void onEnterForeground();
        void onEnterBackground();
    }
}
