package com.iori.custom.common;

import android.os.Handler;

/**
 * restart flow:{@link #restart()},{@link #executeCycle()}
 */
public class CycleModel {
    private boolean execute=true;
    private final Handler executeHandler;
    private long delayTimems;
    private final CycleExecute cycleExecute;

    public CycleModel(Handler executeHandler, CycleExecute cycleExecute) {
        this.executeHandler = executeHandler;
        this.cycleExecute = cycleExecute;
    }

    public void stopExecute(){
        execute=false;
    }

    public void restart(){
        execute=true;
    }

    public void executeCycle(){
        if(execute) {
            executeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cycleExecute.cycleExecute(CycleModel.this);
                }
            },delayTimems);
        }
    }

    public long getDelayTimems() {
        return delayTimems;
    }

    public void setDelayTimems(long delayTimems) {
        if(delayTimems <0 ){
            delayTimems=0;
        }
        this.delayTimems = delayTimems;
    }

    public static interface CycleExecute{
        /**
         * finally code {@link CycleModel#executeCycle()}
         * @param cycleModel
         */
        void cycleExecute(CycleModel cycleModel);
    }
}
