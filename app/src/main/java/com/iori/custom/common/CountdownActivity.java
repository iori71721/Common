package com.iori.custom.common;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.time.Countdown;
import com.iori.custom.common.time.Countdown.*;

import java.util.List;

public class CountdownActivity extends AppCompatActivity {
    private Countdown countdown;
    private EditText inputSec;
    private Button start;
    private Button stop;
    private TextView remainingSec;
    private TextView displayTime;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_activity);
        handler=new Handler();
        inputSec=findViewById(R.id.inputSec);
        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);
        remainingSec=findViewById(R.id.remainingSec);
        displayTime=findViewById(R.id.displayTime);

        setupCountdown();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int setupSec=Integer.parseInt(inputSec.getEditableText().toString());
                countdown.setCountdownIntervalSec(3);
                countdown.setSec(setupSec);
                countdown.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdown.stop();
            }
        });

    }

    private void updateTime(final int remainingSec){
        List<DisplayTime> displayTimeList=Countdown.calcDisplayTime(remainingSec);
        String displayString="";
        for(DisplayTime forDisplayTime:displayTimeList){
            displayString+=forDisplayTime.getValue()+" "+forDisplayTime.getType()+" ";
        }
        final String showString=displayString;
        handler.post(new Runnable() {
            @Override
            public void run() {
                CountdownActivity.this.remainingSec.setText(""+remainingSec+" sec");
                displayTime.setText(showString);
            }
        });
    }

    private void setupCountdown() {
        countdown=new Countdown();
        countdown.setCountdownIntervalSec(3);

        countdown.setCountdownListener(new Countdown.CountdownListener() {
            @Override
            public void start() {
                Log.d("iori_countdown_listener", "start: ");
            }

            @Override
            public void stop(final int remainingSec) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("iori_countdown_listener", "stop : remainingSec "+remainingSec);
                        updateTime(remainingSec);
                        Toast.makeText(CountdownActivity.this,"countdown finish",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void update(int remainingSec) {
                updateTime(remainingSec);
            }

            @Override
            public void finish(int remainingSec) {
                updateTime(remainingSec);
            }
        });
    }

    @Override
    protected void onDestroy() {
        countdown.stop();
        super.onDestroy();
    }
}
