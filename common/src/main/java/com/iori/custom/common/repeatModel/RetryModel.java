package com.iori.custom.common.repeatModel;

public class RetryModel {
    private int retryCount;
    private int alreadyReConnectCount;
    private final RetryBehavior retryBehavior;

    public RetryModel(RetryBehavior retryBehavior) {
        this.retryBehavior = retryBehavior;
    }

    public void reset(){
        alreadyReConnectCount=0;
    }

    public void retry(){
        alreadyReConnectCount++;
        if(alreadyReConnectCount <= retryCount){
            retryBehavior.retry(alreadyReConnectCount);
        }else{
            retryBehavior.retryFail(alreadyReConnectCount);
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
