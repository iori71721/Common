package com.iori.custom.common.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private float lastX;
    private float lastY;
    private View controlView;

    public CustomGestureDetector(View controlView) {
        this.controlView = controlView;
    }

    private void resetScroll(){
        lastX =0f;
        lastY =0f;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        resetScroll();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(e1!=null && e2!=null){
            float scrolldistanceX= e2.getX()-lastX;
            float scrolldistanceY= e2.getY()-lastY;
            controlView.setX(controlView.getX()+scrolldistanceX);
            controlView.setY(controlView.getY()+scrolldistanceY);
            lastX =e2.getX();
            lastY =e2.getY();
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        lastX =e.getX();
        lastY =e.getY();
        return true;
    }
}
