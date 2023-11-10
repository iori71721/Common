package com.iori.custom.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.executeModel.InOrderExecuteModel;
import com.iori.custom.common.executeModel.SingleExecuteModel;
import com.iori.custom.common.image.Screenshoter;
import com.iori.custom.common.reflect.ReflectHelper;
import com.iori.custom.common.repeatModel.CycleModel;
import com.iori.custom.common.repeatModel.RetryModel;
import com.iori.custom.common.service.BaseScreenshotService;
import com.iori.custom.common.service.TestScreenShotService;

public class MainActivity extends AppCompatActivity {
    private final int screenShotRequestCode=1;
    private int cycleCount;
    private Handler handler;
    private RetryModel retryModel;
    private Screenshoter screenShoter;
    private Button topPopWindow;
    private Button cycleTopPopWindow;
    private Button incOrDecCount;
    private int taskCount;
    private Button countDown;
    private Button cycleImageView;
    private Button twoCycleColorView;
    private Button dropListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        handler=new Handler();
        topPopWindow=findViewById(R.id.topPopWindow);
        topPopWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TopPopwindowActviity.class));
            }
        });

        cycleTopPopWindow=findViewById(R.id.cycleTopPopWindow);
        cycleTopPopWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CycleTopPopWindowActivity.class));
            }
        });
        incOrDecCount=findViewById(R.id.incOrDecCount);
        incOrDecCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,IncOrDecCountActivity.class));
            }
        });

        countDown=findViewById(R.id.countDown);
        countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CountdownActivity.class));
            }
        });

        cycleImageView=findViewById(R.id.cycleImageView);
        cycleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CycleImageActivity.class));
            }
        });

        twoCycleColorView=findViewById(R.id.twoCycleColorView);
        twoCycleColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TwoCycleColorActivity.class));
            }
        });

        dropListView=findViewById(R.id.dropListView);
        dropListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,DropListViewActivity.class));
            }
        });

        testMethod();
    }

    private void testMethod(){
//        testCycleModel();
//        testRetryModel();
//        testSingleExecuteModel();
//        testReflectHelper();
//        testScreenShot();
//        testInOrderExecuteModel();
    }

    private void testInOrderExecuteModel(){
        final InOrderExecuteModel<String> inOrderExecuteModel=new InOrderExecuteModel<String>() {
            @Override
            protected void executeTask(final String message, final InOrderExecuteModel<String> executeModel) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("iori_InOrderExecuteModel", "run: task data "+message);
                        executeModel.popAndExecute();
                    }
                },5000);
            }
        };
        inOrderExecuteModel.start();
        inOrderExecuteModel.start();
        inOrderExecuteModel.start();
        final int delayTime=2*1000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    inOrderExecuteModel.addMessage("task "+i);
                    Log.d("iori_InOrderExecuteModel", "run: add task "+i);
                    try {
                        Thread.sleep(delayTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_InOrderExecuteModel", "run: stop and clear");
                inOrderExecuteModel.stopAndClear();
            }
        },25000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case screenShotRequestCode:
                    screenShoter.fetchScreenShot(this,resultCode,data, TestScreenShotService.class);
                    break;
            }
        }
    }

    private void testScreenShot(){
        initScreenShoter();
    }

    private void initScreenShoter() {
        screenShoter = new Screenshoter(new Screenshoter.ScreenShotBehavior() {
            @Override
            public void screenShot(Bitmap bitmap) {
            }

            @Override
            public Intent generateScreenShotServiceIntent(Context context, Class serviceClass, int width, int height, int dpi, int resultCode, Intent onActivityResultScreenCaptureIntent) {
                return BaseScreenshotService.generateBaseScreenShotIntent(context,serviceClass,width,height,dpi,resultCode,onActivityResultScreenCaptureIntent);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                screenShoter.startScreenShot(MainActivity.this,screenShotRequestCode);
            }
        },3000);

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
