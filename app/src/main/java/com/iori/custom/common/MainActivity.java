package com.iori.custom.common;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.executeModel.SingleExecuteModel;
import com.iori.custom.common.repeatModel.CycleModel;
import com.iori.custom.common.repeatModel.RetryModel;

public class MainActivity extends AppCompatActivity {
    private int cycleCount;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler();
//        testCycleModel();
//        testRetryModel();
        testSingleExecuteModel();
    }

    private void testSingleExecuteModel(){
        final SingleExecuteModel singleExecuteModel=new SingleExecuteModel();
        singleExecuteModel.setSingleExecuteBehavior(new SingleExecuteModel.SingleExecuteBehavior() {
            @Override
            public void execute() {
                for(int i=0;i<100;i++){
                    Log.d("iori", "execute: "+i+" thread id "+Thread.currentThread().getId()+" name "+Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i == 99) {
                        singleExecuteModel.finish();
                    }
                }
            }
        });

        Runnable testRunnable=new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        singleExecuteModel.execute();
                    }
                }).start();
            }
        };
        int delayTime=0;
        int stepTime=15000;
        for(int i=0;i<10;i++) {
            delayTime+=stepTime;
            handler.postDelayed(testRunnable,delayTime);
        }
    }

    private void testRetryModel(){
        RetryModel retryModel=new RetryModel(new RetryModel.RetryBehavior() {
            @Override
            public void retry(int alreadyRetryCount) {
                Log.d("iori", "behavior retry: already retry "+alreadyRetryCount);
            }

            @Override
            public void retryFail(int alreadyRetryCount) {
                Log.d("iori", "behavior retryFail: "+alreadyRetryCount);
            }
        });
        int retryCount=5;
        retryModel.setRetryCount(5);
        for(int i=0;i<retryCount+1;i++){
            retryModel.retry();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.d("iori", "testRetryModel: after retryfail execute retry ");
        retryModel.retry();
        Log.d("iori", "testRetryModel: after retryfail execute retry 1");
        retryModel.retry();
        Log.d("iori", "testRetryModel: after retryfail execute retry 2");

        Log.d("iori", "testRetryModel: reset ");
        retryModel.reset();
        Log.d("iori", "testRetryModel: after reset execute retry 1");
        retryModel.retry();
        Log.d("iori", "testRetryModel: after reset execute retry 2");
        retryModel.retry();

    }

    private void testCycleModel(){
        final CycleModel cycleModel=new CycleModel(new Handler(), new CycleModel.CycleExecute() {
            @Override
            public void cycleExecute(CycleModel cycleModel) {
                cycleCount++;
                Log.d("iori", "cycleExecute: "+cycleCount);
                cycleModel.executeCycle();
            }
        });
        cycleModel.setDelayTimems(1000);
        cycleModel.executeCycle();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("iori", "run: stop cycle ");
                cycleModel.stopExecute();
            }
        },5000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("iori", "run: cycle restart");
                cycleCount=0;
                cycleModel.restart();
                cycleModel.executeCycle();
            }
        },10000);
    }
}
