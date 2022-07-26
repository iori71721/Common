package com.iori.custom.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.iori.custom.common.R;

/**
 * useage:
 * 1.setup {@link #cycleColor},{@link #frameColor}
 * 2.setup {@link #framePaddingPx},{@link #frameWidthPx}
 */
public class CycleColorView extends View {
    private final int defaultSizePx =100;
    protected final int defaultColor= Color.BLACK;
    protected final int drawOffsetPx=1;
    private int cycleColor =defaultColor;
    private int frameColor=defaultColor;
    private int framePaddingPx;
    private int frameWidthPx;
    private boolean choose;

    public CycleColorView(Context context) {
        super(context);
    }

    public CycleColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.com_iori_custom_common_CycleColorView);
        cycleColor =typedArray.getColor(R.styleable.com_iori_custom_common_CycleColorView_cycleColor,defaultColor);
        frameColor=typedArray.getColor(R.styleable.com_iori_custom_common_CycleColorView_frameColor,defaultColor);
        framePaddingPx=(int)typedArray.getDimension(R.styleable.com_iori_custom_common_CycleColorView_framePadding,0f);
        frameWidthPx=(int)typedArray.getDimension(R.styleable.com_iori_custom_common_CycleColorView_frameWidth,0f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=calcSize(defaultSizePx,widthMeasureSpec);
        int height=calcSize(defaultSizePx,heightMeasureSpec);

        if(width < height){
            height=width;
        }else{
            width=height;
        }
        setMeasuredDimension(width,height);
    }

    private int calcSize(int defaultSize,int measureSpec){
        int calcSize=defaultSize;
        int mode=MeasureSpec.getMode(measureSpec);
        int size=MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                calcSize=size;
                break;
        }
        return calcSize;
    }

    public void setCycleColorAndReDraw(int color){
        this.cycleColor =color;
        invalidate();
    }

    public void setFrameColorAndReDraw(int color){
        this.frameColor=color;
        invalidate();
    }

    public void setFrameWidthPxAndReDraw(int frameWidthPx){
        this.frameWidthPx=frameWidthPx;
        invalidate();
    }

    public void setFramePaddingPxAndDraw(int framePaddingPx){
        this.framePaddingPx=framePaddingPx;
        invalidate();
    }

    protected void drawCircle(Canvas canvas, int color, int frameWidth, int framePadding){
        Point center=new Point();
        int radius=getMeasuredWidth()/2-frameWidth-framePadding;

        center.x=frameWidth+framePadding+radius;
        center.y=frameWidth+framePadding+radius;

        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    private void drawClickedRect(Canvas canvas,int color,int frameWidthPx) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(frameWidthPx);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0 , getMeasuredWidth()-drawOffsetPx, getMeasuredWidth()-drawOffsetPx, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas, cycleColor, frameWidthPx, framePaddingPx);
        if(choose){
            drawClickedLayout(canvas);
        }
    }

    protected void drawClickedLayout(Canvas canvas) {
        drawClickedRect(canvas,frameColor,frameWidthPx);
    }

    public void choose(boolean choose){
        this.choose=choose;
        invalidate();
    }

    public boolean isChoose() {
        return choose;
    }

    public int getCycleColor() {
        return cycleColor;
    }

    public int getFrameColor() {
        return frameColor;
    }

    public int getFramePaddingPx() {
        return framePaddingPx;
    }

    public int getFrameWidthPx() {
        return frameWidthPx;
    }
}
