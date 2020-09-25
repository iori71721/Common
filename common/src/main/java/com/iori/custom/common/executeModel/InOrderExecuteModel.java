package com.iori.custom.common.executeModel;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * useage
 * 1.constructor
 * 2.{@link #start()}
 * 3.{@link #addMessage(Object)}
 * 4.override {@link #executeTask(Object, InOrderExecuteModel)} then,when execute finish call {@link #popAndExecute()} to execute next task.
 * 5.{@link #stopAndClear()}
 * @param <D> task data
 */
public abstract class InOrderExecuteModel<D> {
    private static final String TAG=InOrderExecuteModel.class.getName();
    private boolean stop=true;
    private BlockingQueue<D> blockingQueue=new LinkedBlockingQueue<>();

    public void popAndExecute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!stop) {
                    D message = takeMessage();
                    executeTask(message, InOrderExecuteModel.this);
                }
            }
        }).start();
    }

    protected abstract void executeTask(D message, InOrderExecuteModel<D> executeModel);

    public void start(){
        if(!stop){
            Log.d(TAG, "start: already start");
            return;
        }else {
            stop = false;
        }
        popAndExecute();
    }

    public void stop(){
        stop=true;
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

    public void stopAndClear(){
        stop();
        blockingQueue.clear();
    }

    public boolean isStop() {
        return stop;
    }
}
