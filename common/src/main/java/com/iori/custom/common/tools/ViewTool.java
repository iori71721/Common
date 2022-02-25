package com.iori.custom.common.tools;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

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

    /**
     * copy from network
     * @param listView
     * @return
     */
    public static int calcListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return params.height;
    }

    /**
     * get view by {@link Activity#setContentView(View)}
     * @param activity
     * @return
     */
    public static View getContentView(Activity activity){
        ViewGroup viewGroup=(ViewGroup) activity.getWindow().getDecorView();
        FrameLayout frameLayout=viewGroup.findViewById(android.R.id.content);
        return frameLayout.getChildAt(0);
    }
}
