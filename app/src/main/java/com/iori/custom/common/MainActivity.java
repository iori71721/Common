package com.iori.custom.common;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.executeModel.SingleExecuteModel;
import com.iori.custom.common.reflect.ReflectHelper;
import com.iori.custom.common.repeatModel.CycleModel;
import com.iori.custom.common.repeatModel.RetryModel;

public class MainActivity extends AppCompatActivity {
    private int cycleCount;
    private Handler handler;
    RetryModel retryModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler=new Handler();
//        testCycleModel();
        testRetryModel();
//        testSingleExecuteModel();
//        testReflectHelper();
    }

    private void testReflectHelper(){
        CycleModel cycleModel=new CycleModel(new Handler(), new CycleModel.CycleExecute() {
            @Override
            public void cycleExecute(CycleModel cycleModel) {

            }
        });

        Log.d("iori", "testReflectHelper: original delayTimems "+cycleModel.getDelayTimems());

        ReflectHelper.setValue(cycleModel, CycleModel.class, "delayTimems", 1500L, new ReflectHelper.ReflectParser<Long>() {
            @Override
            public Long parse(Object parseObject) {
                return Long.parseLong(parseObject.toString());
            }
        });

        long reflectDelayTimems=ReflectHelper.getValue(cycleModel,CycleModel.class,"delayTimems",Long.class);

        Log.d("iori", "testReflectHelper: reflectDelayTimems "+reflectDelayTimems);

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
        final int retryCount=5;
        final int delayMs=0;
        retryModel=new RetryModel(new RetryModel.RetryBehavior() {
            @Override
            public void retry(int alreadyRetryCount) {
                Log.d("iori", "behavior retry: already retry "+alreadyRetryCount);
            }

            @Override
            public void retryFail(int alreadyRetryCount) {
                Log.d("iori", "behavior retryFail: "+alreadyRetryCount);
                Log.d("iori", "retryFail: reset model to do retry ");
                retryModel.reset();
                retryModel.setRetryCount(retryCount);
                retryModel.delayMs=delayMs;
                retryModel.retry();

            }
        });
        retryModel.setRetryCount(retryCount);
        retryModel.delayMs=delayMs;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Log.d("iori", "run: trigger retry");
                    retryModel.retry();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
