package com.iori.custom.common.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;

/**
 * useage:
 * 1.{@link #startScreenShot(Activity, int)}
 * 2.activity#onActivityResult() call {@link #fetchScreenShot(Activity, int, Intent, Class)}
 */
public class Screenshoter {
    private int width;
    private int height;
    private int dpi;
    private MediaProjectionManager mediaProjectionManager;
    private final ScreenShotBehavior screenShotBehavior;

    public Screenshoter(ScreenShotBehavior screenShotBehavior) {
        this.screenShotBehavior=screenShotBehavior;
    }

    public void startScreenShot(Activity activity,int requestCode) {
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
            dpi = displayMetrics.densityDpi;
        }

        mediaProjectionManager = (MediaProjectionManager) activity.getSystemService(MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null) {
            activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), requestCode);
        }
    }

    public void fetchScreenShot(Activity activity,int resultCode, @Nullable Intent data,Class serviceClass){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection != null) {
                getBitmap(mediaProjection,width,height,dpi,screenShotBehavior);
            }
        }else{
            activity.startForegroundService(screenShotBehavior.generateScreenShotServiceIntent(activity,serviceClass,width,height,dpi,resultCode,data));
        }
    }

    public static void getBitmap(MediaProjection mediaProjection,int width,int height,int dpi,final ScreenShotBehavior screenShotBehavior) {
        final ImageReader imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
        mediaProjection.createVirtualDisplay("screen_shot",
                width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                int width = image.getWidth();
                int height = image.getHeight();
                final Image.Plane[] planes = image.getPlanes();
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);
                screenShotBehavior.screenShot(bitmap);
                image.close();
                imageReader.setOnImageAvailableListener(null,null);
            }
        }, null);
    }

    public static interface ScreenShotBehavior{
        void screenShot(Bitmap bitmap);
        Intent generateScreenShotServiceIntent(Context context,Class serviceClass, int width, int height, int dpi,int resultCode,Intent onActivityResultScreenCaptureIntent);
    }
}
