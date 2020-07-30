package com.iori.custom.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.popWindow.SystemTopPopWindow;

public class CycleTopPopWindowActivity extends AppCompatActivity {
    private SystemTopPopWindow<String> systemTopPopWindow;
    private View attachView;
    private Handler handler;
    private int addCount=10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_top_popwindow_activity);
        attachView=findViewById(R.id.attachView);
        handler=new Handler();
        systemTopPopWindow=new SystemTopPopWindow<String>(this,attachView,handler) {
            @Override
            protected View generateContentView(Context context) {
                return LayoutInflater.from(context).inflate(R.layout.cycle_top_popwindow_content_layout,null);
            }

            @Override
            protected void drawMessageLayout(Handler handler,final View messageLayout,final String message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView messageView=messageLayout.findViewById(R.id.message);
                        messageView.setText(message);
                    }
                });
            }
        };
        testAddMessage();
        systemTopPopWindow.start();
    }

    @Override
    protected void onDestroy() {
        systemTopPopWindow.stopAndClear();
        super.onDestroy();
    }

    private void testAddMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageString="";
                for(int i=0;i<addCount;i++){
                    if(i%3==0){
                        messageString=i+"_"+"test";
                    }else if(i%3==1){
                        messageString=i+"_"+"abcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljf" +
                                "abcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljfabcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljf" +
                                "abcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljfabcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljf" +
                                "abcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljfabcdefgfajsfkjasdkjfkasjfklajsklfdjksdjfkasjkfdljaksljf";
                    }else{
                        messageString=i+"_"+"xyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz \nxyz ";
                    }
                    systemTopPopWindow.addMessage(messageString);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
