package com.iori.custom.common.repeatModel;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class RetryModel {
    private static final String TAG=RetryModel.class.getName();
    private int retryCount;
    private int alreadyReConnectCount;
    private final RetryBehavior retryBehavior;
    public int delayMs;
    private boolean firstRetry=true;
    private final Timer timer;
    private int delayRetryCount=0;

    public RetryModel(RetryBehavior retryBehavior) {
        this.retryBehavior = retryBehavior;
        timer=new Timer();
    }

    public void reset(){
        alreadyReConnectCount=0;
        firstRetry=true;
        delayRetryCount=0;
    }

    private void executeRetry(){
        alreadyReConnectCount++;
        if(alreadyReConnectCount <= retryCount){
            retryBehavior.retry(alreadyReConnectCount);
            executeDelayRetry();
        }else{
            retryBehavior.retryFail(alreadyReConnectCount);
        }
    }

    private void executeDelayRetry(){
        if(delayRetryCount > 0){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    delayRetryCount--;
                    if(delayRetryCount < 0){
                        delayRetryCount=0;
                    }
                    executeRetry();
                }
            },delayMs);
        }
    }

    public void retry(){
        if(firstRetry) {
            executeRetry();
            firstRetry=false;
        }else{
            if(delayMs>0) {
                delayRetryCount++;
                if(delayRetryCount == 1) {
                    executeDelayRetry();
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

    public int getDelayRetryCount() {
        return delayRetryCount;
    }

    public int getAlreadyReConnectCount() {
        return alreadyReConnectCount;
    }

    public static interface RetryBehavior{
        void retry(int alreadyRetryCount);
        void retryFail(int alreadyRetryCount);
    }
}
