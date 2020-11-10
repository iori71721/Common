package com.iori.custom.common.popWindow;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * useage
 * 1.constructor
 * 2.{@link #start()}
 * 3.{@link #addMessage(Object)}
 * 4.when activity destroy call {@link #stop()}
 * @param <D> window data
 */
public abstract class SystemTopPopWindow<D> {
    private static final String TAG=SystemTopPopWindow.class.getName();
    private BlockingQueue<D> blockingQueue=new LinkedBlockingQueue<>();
    private FullWidthTopPopWindow fullWidthTopPopWindow;
    private final Context context;
    private View contentView;
    private View attachView;
    private final Handler handler;
    public int delayCloseMs=2*1000;
    public int delayAfterUserDismissMs=10*1000;
    private boolean showFinish=false;
    private boolean stop=false;

    public SystemTopPopWindow(Context context, View attachView, final Handler handler) {
        this.context=context;
        this.handler=handler;
        this.attachView=attachView;
        contentView=generateContentView(context);
        initFullWidthTopPopWindow(context,contentView,attachView);
    }

    private void initFullWidthTopPopWindow(Context context, final View contentView, View attachView){
        fullWidthTopPopWindow=new FullWidthTopPopWindow(context,contentView,attachView);
        fullWidthTopPopWindow.setFullWidthTopPopWindowDisplayCallback(new FullWidthTopPopWindow.FullWidthTopPopWindowDisplayCallback() {
            @Override
            public void startShow() {
                showFinish=false;
            }

            @Override
            public void endShow() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fullWidthTopPopWindow.close();
                    }
                },delayCloseMs);

            }

            @Override
            public void startClose() {

            }

            @Override
            public void endClose() {
                showFinish=true;
                if(!stop) {
                    autoShow();
                }
            }
        });
        fullWidthTopPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(!showFinish) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            autoShow();
                        }
                    }, delayAfterUserDismissMs);
                }
            }
        });
    }

    protected abstract View generateContentView(Context context);

    protected abstract void drawMessageLayout(Handler handler, View messageLayout, D message);

    private void showMessage(Handler handler, View messageLayout, D message){
        if(stop){
            return;
        }
        drawMessageLayout(handler,messageLayout,message);
        if(attachView != null) {
            attachView.post(new Runnable() {
                @Override
                public void run() {
                    fullWidthTopPopWindow.show();
                }
            });
        }
    }

    private void autoShow(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                D addMessage=takeMessage();
                showMessage(handler,contentView,addMessage);
            }
        }).start();
    }

    private D takeMessage(){
        D message=null;
        try {
            message=blockingQueue.take();
        } catch (InterruptedException e) {
            Log.i(TAG, "takeMessage: error "+e);
            e.printStackTrace();
        }
        return message;
    }

    public void addMessage(D message){
        try {
            blockingQueue.put(message);
        } catch (InterruptedException e) {
            Log.i(TAG, "addMessage: error "+e);
            e.printStackTrace();
        }
    }

    public void start(){
        stop=false;
        autoShow();
    }

    public void stop(){
        stop=true;
    }

    public void stopAndClear(){
        stop();
        blockingQueue.clear();
    }
}
