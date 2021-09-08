package com.iori.custom.common.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

public class ViewTool {
    public static int setGridViewHeightBasedOnChildren(GridView gridView, int fixedHeight) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return -1;
        }

        int col = gridView.getNumColumns();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i ++) {
            View listItem = listAdapter.getView(i, null, gridView);
            if(i%col==0) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight()+gridView.getVerticalSpacing();
            }
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        totalHeight+=fixedHeight;
        params.height = totalHeight;
        gridView.setLayoutParams(params);
        return totalHeight;
    }
}
