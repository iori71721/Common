package com.iori.custom.common.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

public class TestScreenShotService extends BaseScreenshotService {
    @Override
    protected void afterScreenShot(Bitmap bitmap) {
    }

    @Override
    protected Intent generateScreenShotIntent(Context context, Class serviceClass, int width, int height, int dpi, int resultCode, Intent onActivityResultScreenCaptureIntent) {
        return BaseScreenshotService.generateBaseScreenShotIntent(context,serviceClass,width,height,dpi,resultCode,onActivityResultScreenCaptureIntent);
    }

    @Override
    protected void initNotificationInfos() {
        notificationID=NotificationIDs.TEST_SCREEN_SHOT_SERVICE;
        channelID=getClass().getSimpleName();
    }
}
