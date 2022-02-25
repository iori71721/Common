package com.iori.custom.common.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionTool {
    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    public static boolean AllPermissionsGranted(Context context, String[] requestPermissions){
        boolean allPermissionsGranted=true;
        for(String checkPermission:requestPermissions){
            if(ContextCompat.checkSelfPermission(context,checkPermission) != PackageManager.PERMISSION_GRANTED){
                allPermissionsGranted=false;
                break;
            }
        }
        return allPermissionsGranted;
    }

    public static void requestAllPermissions(Activity activity, String[] requestPermissions, int requestCode){
        ActivityCompat.requestPermissions(activity,requestPermissions,requestCode);
    }

    public static boolean checkAllRequestPermissionsGrant(@NonNull int[] grantResults){
        boolean allRequestPermissionsGrant=true;
        if(grantResults.length==0){
            return false;
        }

        for(int grantResult:grantResults){
            if(grantResult != PackageManager.PERMISSION_GRANTED){
                allRequestPermissionsGrant=false;
                break;
            }
        }

        return allRequestPermissionsGrant;
    }
}
