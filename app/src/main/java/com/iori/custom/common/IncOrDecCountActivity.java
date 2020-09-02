package com.iori.custom.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.iori.custom.common.ui.IncOrDecCounter;

public class IncOrDecCountActivity extends Activity {
    IncOrDecCounter incOrDecCounter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inc_or_dec_count_activity);
        View rootView=findViewById(R.id.incOrDecCount);
        incOrDecCounter=new IncOrDecCounter(rootView);
        incOrDecCounter.setMinCount(-9);
        incOrDecCounter.setMaxCount(200);
    }
}
