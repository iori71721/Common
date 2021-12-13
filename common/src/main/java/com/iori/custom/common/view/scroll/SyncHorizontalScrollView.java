package com.iori.custom.common.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * useage:
 * 1.constructor
 * 2.{@link #addSyncView(View)}
 */
public class SyncHorizontalScrollView extends HorizontalScrollView {
    private final List<View> syncScrollViews=new ArrayList<>(10);

    public SyncHorizontalScrollView(Context context) {
        super(context);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        synchronized (syncScrollViews){
            for(View syncView:syncScrollViews){
                syncView.scrollTo(l,t);
            }
        }
    }

    public void addSyncView(View view){
        synchronized (syncScrollViews){
            syncScrollViews.add(view);
        }
    }

}
