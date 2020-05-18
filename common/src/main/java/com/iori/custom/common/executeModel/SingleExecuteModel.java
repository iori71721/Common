package com.iori.custom.common.executeModel;

import android.util.Log;

/**
 * only single executer,when is executing,other executer will by rejected.
 * useage:1.new object
 * 2.{@link #setSingleExecuteBehavior(SingleExecuteBehavior)}
 * 3.{@link #execute()}
 * 4.when execute finish call {@link #finish()}
 */
public class SingleExecuteModel {
    private boolean executing;
    private SingleExecuteBehavior singleExecuteBehavior;

    public void execute(){
        synchronized (this) {
            if (executing) {
                return;
            }
        }
        if(singleExecuteBehavior != null){
            synchronized (this) {
                if (!executing) {
                    executing = true;
                }
            }
            singleExecuteBehavior.execute();
        }
    }

    public synchronized void finish(){
        executing=false;
    }

    public static interface SingleExecuteBehavior{
        void execute();
    }

    public SingleExecuteBehavior getSingleExecuteBehavior() {
        return singleExecuteBehavior;
    }

    public void setSingleExecuteBehavior(SingleExecuteBehavior singleExecuteBehavior) {
        this.singleExecuteBehavior = singleExecuteBehavior;
    }
}
