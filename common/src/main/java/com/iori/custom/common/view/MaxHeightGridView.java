package com.iori.custom.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MaxHeightGridView extends GridView {
    private int maxHeight=150;

    public MaxHeightGridView(Context context) {
        super(context);
    }

    public MaxHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int setupWidth=width;
        int setupHeight=Math.min(height,maxHeight);
        if(calcSingleItemHeight() > 0){
            setupHeight=Math.min(setupHeight, calcRealRowsHeight());
        }
        setMeasuredDimension(setupWidth,setupHeight);
    }

    private int calcRealRowsHeight(){
        int totalItemHeight=0;
        if(getChildCount() > 0 && getNumColumns()>0){
            for(int i=0;i<getChildCount();i++){
                if(i%getNumColumns() ==0) {
                    totalItemHeight += getChildAt(i).getHeight();
                }
            }
        }
        return totalItemHeight;
    }

    private int calcSingleItemHeight(){
        int itemHeight=0;
        if(getChildCount() > 0){
            for(int i=0;i<getChildCount();i++){
                itemHeight=getChildAt(i).getHeight();
                break;
            }
        }
        return itemHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }
}
