package com.iori.custom.common.popWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

public class FullWidthTopPopWindow extends PopupWindow {
    private final View attachView;
    private final View contentView;
    private int contentViewWidth;
    private int contentViewHeight;
    private int animationMs=2*1000;
    private FullWidthTopPopWindowDisplayCallback fullWidthTopPopWindowDisplayCallback;
    private Context context;
    public FullWidthTopPopWindow(Context context, final View contentView, View attachView) {
        super(context);
        this.context=context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setOutsideTouchable(false);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(contentView);
        this.attachView=attachView;
        this.contentView=contentView;
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentViewWidth=contentView.getWidth();
                contentViewHeight=contentView.getHeight();
            }
        });
    }

    private int calcAnimationMs(int height){
        int animationDuration;
        final int baseHeight=(int)(100*context.getResources().getDisplayMetrics().density);
        final int baseAnimationDurationMs=1*1000;
        if(height > baseHeight){
            animationDuration=height/baseHeight*baseAnimationDurationMs;
        }else{
            animationDuration=baseAnimationDurationMs;
        }
        return animationDuration;
    }

    public void show() {
        showAsDropDown(attachView);

        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentViewWidth=contentView.getWidth();
                contentViewHeight=contentView.getHeight();
                Animation am = new TranslateAnimation(attachView.getLeft(), attachView.getLeft(), attachView.getTop() - contentViewHeight, attachView.getTop());
                animationMs=calcAnimationMs(contentViewHeight);
                am.setDuration(animationMs);
                am.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(fullWidthTopPopWindowDisplayCallback != null){
                            fullWidthTopPopWindowDisplayCallback.startShow();
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(fullWidthTopPopWindowDisplayCallback != null){
                            fullWidthTopPopWindowDisplayCallback.endShow();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                contentView.startAnimation(am);
            }
        });
    }

    public void close() {
        if (!isShowing()) {
            return;
        }
        Animation am = new TranslateAnimation(attachView.getLeft(), attachView.getLeft(), attachView.getTop(), attachView.getTop() - contentViewHeight);
        am.setDuration(animationMs);
        am.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(fullWidthTopPopWindowDisplayCallback != null){
                    fullWidthTopPopWindowDisplayCallback.startClose();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(fullWidthTopPopWindowDisplayCallback != null){
                    fullWidthTopPopWindowDisplayCallback.endClose();
                }
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        contentView.startAnimation(am);
    }

    public void setFullWidthTopPopWindowDisplayCallback(FullWidthTopPopWindowDisplayCallback fullWidthTopPopWindowDisplayCallback) {
        this.fullWidthTopPopWindowDisplayCallback = fullWidthTopPopWindowDisplayCallback;
    }

    public static interface FullWidthTopPopWindowDisplayCallback{
        void startShow();
        void endShow();
        void startClose();
        void endClose();
    }
}
