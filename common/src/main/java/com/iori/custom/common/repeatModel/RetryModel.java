package com.iori.custom.common.repeatModel;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class RetryModel {
    private static final String TAG=RetryModel.class.getName();
    private int retryCount;
    private int alreadyReConnectCount;
    private final RetryBehavior retryBehavior;
    public int delayMs;
    private boolean firstRetry=true;
    private final Timer timer;
    private long lastRetryTime;

    public RetryModel(RetryBehavior retryBehavior) {
        this.retryBehavior = retryBehavior;
        timer=new Timer();
    }

    public void reset(){
        alreadyReConnectCount=0;
        firstRetry=true;
        delayMs=0;
        lastRetryTime=0;
    }

    private void executeRetry(){
        alreadyReConnectCount++;
        if(alreadyReConnectCount <= retryCount){
            retryBehavior.retry(alreadyReConnectCount);
        }else{
            retryBehavior.retryFail(alreadyReConnectCount);
        }
        lastRetryTime= Calendar.getInstance().getTimeInMillis();
    }

    private boolean canExecuteDelayRetry(){
        long currentRetryTime=Calendar.getInstance().getTimeInMillis();
        long canExecuteTime=lastRetryTime+delayMs;
        boolean canExecute=false;
        if(currentRetryTime >= canExecuteTime){
                canExecute=true;
        }
        return canExecute;
    }

    public void retry(){
        if(firstRetry) {
            executeRetry();
            firstRetry=false;
        }else{
            if(delayMs>0) {
                if(canExecuteDelayRetry()) {
                    executeRetry();
                }else{
                    Log.d(TAG, "retry: delay retry too early last retry time "+new Date(lastRetryTime));
                }
            }else {
                executeRetry();
            }
        }
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        if(retryCount < 0){
            retryCount=0;
        }
        this.retryCount = retryCount;
    }

    public static interface RetryBehavior{
        void retry(int alreadyRetryCount);
        void retryFail(int alreadyRetryCount);
    }
}
