package com.iori.custom.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Size;
import android.view.SurfaceView;

public class RatioSurfaceView extends SurfaceView {
    /**
     * after used {@link #resize(Size, int, int, boolean)}
     */
    private int ratioWidth = 0;
    /**
     * after used {@link #resize(Size, int, int, boolean)}
     */
    private int ratioHeight = 0;
    public boolean fullLayout=true;

    public RatioSurfaceView(Context context) {
        super(context);
    }

    public RatioSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 >= ratioWidth || 0 >= ratioHeight) {
            setMeasuredDimension(width, height);
        } else {
            setMeasuredDimension(ratioWidth,ratioHeight);
        }
    }

    public Size resize(Size suitablePreviewSize, int expectPreviewWidth, int expectPreviewHeight, boolean portrait){
        int calcBaseWidth=suitablePreviewSize.getWidth();
        int calcBaseHeight=suitablePreviewSize.getHeight();
        if(portrait){
            calcBaseWidth=suitablePreviewSize.getHeight();
            calcBaseHeight=suitablePreviewSize.getWidth();
        }

        Size resize=new Size(calcBaseWidth,calcBaseHeight);
        if (0 >= expectPreviewWidth || 0 >= expectPreviewHeight) {
            throw new RuntimeException("invalid size to resize SurfaceView");
        } else {
            int setupWidth;
            int setupHeight;
            if (expectPreviewWidth < expectPreviewHeight * calcBaseWidth / calcBaseHeight) {
                setupWidth=expectPreviewWidth;
                setupHeight=expectPreviewWidth * calcBaseHeight / calcBaseWidth;
            } else {
                setupWidth=expectPreviewHeight * calcBaseWidth/ calcBaseHeight;
                setupHeight=expectPreviewHeight;
                setMeasuredDimension(setupWidth, setupHeight);
            }

            float fullScale=1.0f;
            if(fullLayout){
                float widthScale=expectPreviewWidth*1.0f/setupWidth;
                float heightScale=expectPreviewHeight*1.0f/setupHeight;
                fullScale=widthScale>heightScale?widthScale:heightScale;
                setupWidth*=fullScale;
                setupHeight*=fullScale;
            }
            resize=new Size(setupWidth,setupHeight);
            ratioWidth =resize.getWidth();
            ratioHeight =resize.getHeight();
            requestLayout();
        }
        return resize;
    }

    public int getRatioWidth() {
        return ratioWidth;
    }

    public int getRatioHeight() {
        return ratioHeight;
    }

    public boolean isFullLayout() {
        return fullLayout;
    }
}
