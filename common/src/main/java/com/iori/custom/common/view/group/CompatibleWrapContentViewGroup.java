package com.iori.custom.common.view.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public abstract class CompatibleWrapContentViewGroup extends ViewGroup {
    private int measureWidth;
    private int measureHeight;

    public CompatibleWrapContentViewGroup(Context context) {
        super(context);
    }

    public CompatibleWrapContentViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatibleWrapContentViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CompatibleWrapContentViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        int count = getChildCount();
        int maxWidth = 0;
        int totalHeight = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int extraWidth = widthSize - paddingLeft - paddingRight;
//<!--直接用measureChildren測量所有的子View的高度-->
        measureChildren(widthMeasureSpec, heightMeasureSpec);
//<!--現在可以獲得所有子View的尺寸-->
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view != null && view.getVisibility() != GONE) {
                if (lineWidth + view.getMeasuredWidth() > extraWidth) {
                    totalHeight += lineHeight ;
                    lineWidth = view.getMeasuredWidth();
                    lineHeight = view.getMeasuredHeight();
                    maxWidth = widthSize;
                } else {
                    lineWidth += view.getMeasuredWidth();
                }
//<!--獲取每行的最高View尺寸-->
                lineHeight = Math.max(lineHeight, view.getMeasuredHeight());
            }
        }
//        總高度為滿行高度加上未滿行的高度
        totalHeight = Math.max(totalHeight + lineHeight, lineHeight);
        maxWidth = Math.max(lineWidth, maxWidth);
//<!--totalHeight 跟 maxWidth都是FlowLayout渴望得到的尺寸-->
//<!--至於合不合適，通過resolveSize再來判斷一遍，當然，如果你非要按照自己的尺寸來，也可以設定，但是不太合理-->

        totalHeight = resolveSize(totalHeight + paddingBottom + paddingTop, heightMeasureSpec);
        lineWidth = resolveSize(maxWidth + paddingLeft + paddingRight, widthMeasureSpec);

        measureWidth=lineWidth;
        measureHeight=totalHeight;
        setMeasuredDimension(lineWidth, totalHeight);
    }

    /**
     * keep
     * @param parent
     * @return
     */
    private int getChildHeight(View parent){
        MarginLayoutParams marginLayoutParams=null;
        if(parent.getLayoutParams() instanceof MarginLayoutParams){
            marginLayoutParams=(MarginLayoutParams) parent.getLayoutParams();
        }
        if(parent instanceof ViewGroup){
            ViewGroup viewGroup=(ViewGroup) parent;
            int totalHeight=0;
            for(int i=0;i<viewGroup.getChildCount();i++){
                totalHeight+=getChildHeight(viewGroup.getChildAt(i));
            }
            return totalHeight;
        }else{
            int height=parent.getMeasuredHeight()+parent.getPaddingTop()
                    +parent.getPaddingBottom();
            if(marginLayoutParams != null) {
                height=height+marginLayoutParams.topMargin
                        + marginLayoutParams.bottomMargin;
            }
            return height;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int currentHeight = 0;

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();

        for (int i = 0 ; i < count ; i++) {
            View view = getChildAt(i);
            int height = view.getMeasuredHeight();
            int width = view.getMeasuredWidth();

//            修正padding 起點
            view.layout(0, currentHeight, width, currentHeight + height);
            currentHeight += height;
        }
    }

    public int getMeasureWidth() {
        return measureWidth;
    }

    public int getMeasureHeight() {
        return measureHeight;
    }
}
