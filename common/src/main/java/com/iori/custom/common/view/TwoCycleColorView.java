package com.iori.custom.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.iori.custom.common.R;

public class TwoCycleColorView extends CycleColorView{
    private final int DEFAULT_OUT_SIDE_STROKE_WIDTH=10;

    private int outSideStrokeWidth=10;

    public TwoCycleColorView(Context context) {
        super(context);
    }

    public TwoCycleColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.com_iori_custom_common_TwoCycleColorView);
        int setupOutSideStrokeWidth=(int)typedArray.getDimension(R.styleable.com_iori_custom_common_TwoCycleColorView_outSideStrokeWidth,DEFAULT_OUT_SIDE_STROKE_WIDTH);
        outSideStrokeWidth=setupOutSideStrokeWidth;

        typedArray.recycle();
    }

    @Override
    protected void drawCircle(Canvas canvas, int color, int frameWidth, int framePadding) {
        Point center=new Point();
        int radius=getMeasuredWidth()/2;
        center.x=radius;
        center.y=radius;
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    @Override
    protected void drawClickedLayout(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

//        inside cycle
        Point center=new Point();
        int radius=getMeasuredWidth()/2;
        center.x=radius;
        center.y=radius;
        radius/=2;
        Paint paint = new Paint();
        paint.setColor(getCycleColor());
        canvas.drawCircle(center.x, center.y, radius, paint);

//        outside cycle
        radius=getMeasuredWidth()/2-outSideStrokeWidth/2;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) outSideStrokeWidth);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public int getOutSideStrokeWidth() {
        return outSideStrokeWidth;
    }

    public void setOutSideStrokeWidth(int outSideStrokeWidth) {
        this.outSideStrokeWidth = outSideStrokeWidth;
    }
}
