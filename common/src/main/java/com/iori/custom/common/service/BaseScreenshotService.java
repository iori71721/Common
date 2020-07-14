package com.iori.custom.common.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.iori.custom.common.image.Screenshoter;

public abstract class BaseScreenshotService extends BaseForegroundService{
    public static final String INTENT_KEY_INT_WIDTH ="INTENT_INT_WIDTH";
    public static final String INTENT_KEY_INT_HEIGHT ="INTENT_INT_HEIGHT";
    public static final String INTENT_KEY_INT_DPI ="INTENT_INT_DPI";
    public static final String INTENT_KEY_INT_RESULT_CODE="INTENT_KEY_INT_RESULT_CODE";
    public static final String INTENT_KEY_INTENT_ON_ACTIVITY_RESULT_SCREEN_CAPTURE_INTENT="INTENT_KEY_INTENT_ON_ACTIVITY_RESULT_SCREEN_CAPTURE_INTENT";
    public static final int DELAY_AFTER_USER_ALLOW_SCREEN_SHOT_MS=1000;
    private int width;
    private int height;
    private int dpi;
    private int resultCode;
    private Intent onActivityResultScreenCaptureIntent;
    private MediaProjectionManager mediaProjectionManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            if(intent.getExtras() != null) {
                loadData(intent);
                startScreenShot();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void loadData(@NonNull Intent intent){
        Bundle bundle=intent.getExtras();
        if(bundle != null){
            width=bundle.getInt(INTENT_KEY_INT_WIDTH);
            height=bundle.getInt(INTENT_KEY_INT_HEIGHT);
            dpi=bundle.getInt(INTENT_KEY_INT_DPI);
            resultCode=bundle.getInt(INTENT_KEY_INT_RESULT_CODE);
            onActivityResultScreenCaptureIntent=bundle.getParcelable(INTENT_KEY_INTENT_ON_ACTIVITY_RESULT_SCREEN_CAPTURE_INTENT);
        }
    }

    private void startScreenShot(){
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, onActivityResultScreenCaptureIntent);

        try {
            Thread.sleep(DELAY_AFTER_USER_ALLOW_SCREEN_SHOT_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mediaProjection != null) {
            Screenshoter.getBitmap(mediaProjection, width, height, dpi, new Screenshoter.ScreenShotBehavior() {
                @Override
                public void screenShot(Bitmap bitmap) {
                    if(bitmap != null){
                        afterScreenShot(bitmap);
                    }
                }

                @Override
                public Intent generateScreenShotServiceIntent(Context context, Class serviceClass, int width, int height, int dpi, int resultCode, Intent onActivityResultScreenCaptureIntent) {
                    return generateScreenShotIntent(context,serviceClass,width,height,dpi,resultCode,onActivityResultScreenCaptureIntent);
                }
            });
        }
    }

    protected abstract void afterScreenShot(Bitmap bitmap);

    protected abstract Intent generateScreenShotIntent(Context context, Class serviceClass, int width, int height, int dpi, int resultCode, Intent onActivityResultScreenCaptureIntent);

    public static Intent generateBaseScreenShotIntent(Context context, Class serviceClass, int width, int height, int dpi, int resultCode, Intent onActivityResultScreenCaptureIntent){
        Intent intent=new Intent(context,serviceClass);
        intent.putExtra(INTENT_KEY_INT_WIDTH,width);
        intent.putExtra(INTENT_KEY_INT_HEIGHT,height);
        intent.putExtra(INTENT_KEY_INT_DPI,dpi);
        intent.putExtra(INTENT_KEY_INT_RESULT_CODE,resultCode);
        intent.putExtra(INTENT_KEY_INTENT_ON_ACTIVITY_RESULT_SCREEN_CAPTURE_INTENT,onActivityResultScreenCaptureIntent);
        return intent;
    }
}
