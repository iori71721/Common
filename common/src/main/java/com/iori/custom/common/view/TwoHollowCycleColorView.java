package com.iori.custom.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.iori.custom.common.R;

public class TwoHollowCycleColorView extends CycleColorView{
    private int hollowCycleColor= Color.WHITE;
    private int hollowCycleStrokeWidth;

    public TwoHollowCycleColorView(Context context) {
        super(context);
    }

    public TwoHollowCycleColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.com_iori_custom_common_TwoHollowCycleColorView);
        int setupColor=typedArray.getColor(R.styleable.com_iori_custom_common_TwoHollowCycleColorView_hollowCycleColor,Color.WHITE);
        hollowCycleColor=setupColor;
        hollowCycleStrokeWidth =(int)typedArray.getDimension(R.styleable.com_iori_custom_common_TwoHollowCycleColorView_hollowCycleStrokeWidth,5.0f);

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

        radius=getMeasuredWidth()/2-hollowCycleStrokeWidth/2;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) hollowCycleStrokeWidth);
        paint.setColor(hollowCycleColor);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    @Override
    protected void drawClickedLayout(Canvas canvas) {
        Point center=new Point();
        int radius=getMeasuredWidth()/2;
        center.x=radius;
        center.y=radius;

        radius=radius/2-hollowCycleStrokeWidth/2;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) hollowCycleStrokeWidth);
        paint.setColor(hollowCycleColor);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public int getHollowCycleColor() {
        return hollowCycleColor;
    }

    public void setHollowCycleColor(int hollowCycleColor) {
        this.hollowCycleColor = hollowCycleColor;
    }

    public int getHollowCycleStrokeWidth() {
        return hollowCycleStrokeWidth;
    }

    public void setHollowCycleStrokeWidth(int hollowCycleStrokeWidth) {
        this.hollowCycleStrokeWidth = hollowCycleStrokeWidth;
    }
}
