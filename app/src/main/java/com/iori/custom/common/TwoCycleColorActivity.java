package com.iori.custom.common;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.view.CycleColorView;
import com.iori.custom.common.view.TwoCycleColorView;
import com.iori.custom.common.view.TwoHollowCycleColorView;

public class TwoCycleColorActivity extends AppCompatActivity {
    private CycleColorView cycleColorView;
    private TwoCycleColorView twoCycleColorView;
    private CycleColorView cycleColorView2;
    private TwoCycleColorView twoCycleColorView2;
    private CycleColorView cycleColorView3;
    private TwoCycleColorView twoCycleColorView3;
    private CycleColorView cycleColorView4;
    private TwoCycleColorView twoCycleColorView4;

    private TwoHollowCycleColorView twoHollowCycleColorView;
    private TwoHollowCycleColorView twoHollowCycleColorView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_cycle_color_activity);

        cycleColorView=findViewById(R.id.cycleColorView);
        cycleColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(cycleColorView);
            }
        });

        twoCycleColorView=findViewById(R.id.twoCycleColorView);
        twoCycleColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoCycleColorView);
            }
        });

        cycleColorView2=findViewById(R.id.cycleColorView2);
        cycleColorView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(cycleColorView2);
            }
        });

        twoCycleColorView2=findViewById(R.id.twoCycleColorView2);
        twoCycleColorView2.setOutSideStrokeWidth(30);
        twoCycleColorView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoCycleColorView2);
            }
        });

        cycleColorView3=findViewById(R.id.cycleColorView3);
        cycleColorView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(cycleColorView3);
            }
        });

        twoCycleColorView3=findViewById(R.id.twoCycleColorView3);
        twoCycleColorView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoCycleColorView3);
            }
        });

        cycleColorView4=findViewById(R.id.cycleColorView4);
        cycleColorView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(cycleColorView4);
            }
        });

        twoCycleColorView4=findViewById(R.id.twoCycleColorView4);
        twoCycleColorView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoCycleColorView4);
            }
        });

        twoHollowCycleColorView=findViewById(R.id.twoHollowCycleColorView);
        twoHollowCycleColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoHollowCycleColorView);
            }
        });

        twoHollowCycleColorView2=findViewById(R.id.twoHollowCycleColorView2);
        twoHollowCycleColorView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleViewClick(twoHollowCycleColorView2);
            }
        });
    }

    private void cycleViewClick(CycleColorView cycleColorView){
        cycleColorView.choose(!cycleColorView.isChoose());
    }
}
