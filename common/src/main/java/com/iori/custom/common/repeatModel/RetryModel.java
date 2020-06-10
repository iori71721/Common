package com.iori.custom.common.repeatModel;

import android.util.Log;

import java.util.Calendar;
import java.util.Timer;

public class RetryModel {
    private int retryCount;
    private int alreadyReConnectCount;
    private final RetryBehavior retryBehavior;
    public int delayMs;
    private boolean firstRetry=true;
    private final Timer timer;
    private long lastRetryTime;
    private long nextDelayRetryTime;

    public RetryModel(RetryBehavior retryBehavior) {
        this.retryBehavior = retryBehavior;
        timer=new Timer();
    }

    public void reset(){
        alreadyReConnectCount=0;
        firstRetry=true;
        delayMs=0;
        lastRetryTime=0;
        nextDelayRetryTime=0;
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
        if(currentRetryTime > canExecuteTime){
            if(currentRetryTime > nextDelayRetryTime){
                canExecute=true;
            }
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
                    nextDelayRetryTime=Calendar.getInstance().getTimeInMillis()+delayMs;
                    executeRetry();
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
