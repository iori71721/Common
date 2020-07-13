package com.iori.custom.common;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.popWindow.FullWidthTopPopWindow;

public class TopPopwindowActviity extends AppCompatActivity {
    private LinearLayout root;
    private View attachView;
    private FullWidthTopPopWindow fullWidthTopPopWindow;
    private Handler handler;
    private Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_popwindow_activity);
        root=findViewById(R.id.root);
        attachView=findViewById(R.id.attachView);
        button=findViewById(R.id.button);
        handler=new Handler();
        final View messageLayout= LayoutInflater.from(this).inflate(R.layout.top_popwindow_layout,null);
        final TextView messageTextView=messageLayout.findViewById(R.id.message);
        messageLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_popwindow", "run: "+" messageLayout.getWidth() "+messageLayout.getWidth()+" messageLayout.getHeight() "+messageLayout.getHeight());
            }
        });
        fullWidthTopPopWindow=new FullWidthTopPopWindow(this,messageLayout,attachView);
        
        fullWidthTopPopWindow.setFullWidthTopPopWindowDisplayCallback(new FullWidthTopPopWindow.FullWidthTopPopWindowDisplayCallback() {
            @Override
            public void startShow() {
            }

            @Override
            public void endShow() {
            }

            @Override
            public void startClose() {
            }

            @Override
            public void endClose() {
            }
        });
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TopPopwindowActviity.this, "show message", Toast.LENGTH_SHORT).show();
                messageTextView.setText("start show");
                fullWidthTopPopWindow.show();
                Log.d("iori_popwindow", "after show : width "+fullWidthTopPopWindow.getWidth()+" height "+fullWidthTopPopWindow.getHeight());
            }
        },2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TopPopwindowActviity.this,"close message",Toast.LENGTH_SHORT).show();
                messageTextView.setText("close show");
                fullWidthTopPopWindow.close();
            }
        },10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TopPopwindowActviity.this,"show message",Toast.LENGTH_SHORT).show();
                messageTextView.setText("start show2");
                fullWidthTopPopWindow.show();
            }
        },15000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TopPopwindowActviity.this,"click button",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
