package com.iori.custom.common.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewTool {
    public static void visible(View view, boolean visible){
        if(visible){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void closeKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
