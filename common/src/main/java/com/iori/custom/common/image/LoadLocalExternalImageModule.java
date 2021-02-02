package com.iori.custom.common.image;

import android.app.Activity;
import android.content.Intent;

import java.io.File;

/**
 * useage:
 * 1.constructor
 * 2.{@link #loadImage(Activity, int)}
 * 3.{@link #delegateOnActivityResult(Activity, int, int, Intent)} in {@link Activity#onActivityResult(int, int, Intent)}
 */
public class LoadLocalExternalImageModule {
    private int requestCode;
    private final LoadLocalExternalImageModuleBehavior loadLocalExternalImageModuleBehavior;

    public LoadLocalExternalImageModule(LoadLocalExternalImageModuleBehavior loadLocalExternalImageModuleBehavior) {
        this.loadLocalExternalImageModuleBehavior = loadLocalExternalImageModuleBehavior;
    }

    public void loadImage(Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
        this.requestCode=requestCode;
    }

    public void delegateOnActivityResult(Activity activity,int requestCode, int resultCode, Intent data) {
        if(loadLocalExternalImageModuleBehavior == null){
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (this.requestCode == requestCode) {
                String filePath = ImageTool.parsePath(activity, data.getData());
                if (filePath != null) {
                    File loadFile = new File(filePath);
                    if (loadFile.exists()) {
                        loadLocalExternalImageModuleBehavior.loadSuccess(filePath);
                    } else {
                        loadLocalExternalImageModuleBehavior.loadFail();
                    }
                } else {
                    loadLocalExternalImageModuleBehavior.loadFail();
                }
            }
        }
    }

    public static interface LoadLocalExternalImageModuleBehavior{
        void loadSuccess(String imagePath);
        void loadFail();
    }
}
