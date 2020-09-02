package com.iori.custom.common.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iori.custom.common.R;

public class IncOrDecCounter{
    private final String TAG=getClass().getSimpleName();
    protected final int defaultCount=0;
    private Button inc;
    private Button dec;
    private EditText countEditText;
    private int count=1;
    private int minCount=defaultCount;
    private int maxCount=99999;

    public IncOrDecCounter(View rootView){
        initLayout(rootView);
    }

    protected void initLayout(View rootView){
        inc=rootView.findViewById(R.id.inc);
        dec=rootView.findViewById(R.id.dec);
        countEditText=rootView.findViewById(R.id.count);

        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incClick(v);
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decClick(v);
            }
        });
        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setInputCount(s.toString());
            }
        });
        inc.post(new Runnable() {
            @Override
            public void run() {
                countEditText.setWidth(inc.getWidth()+40);
            }
        });
    }

    protected void incClick(View v){
        incCount();
    }

    protected void decClick(View v){
        decCount();
    }

    protected void setInputCount(String countString){
        int setupCount=count;
        try {
            setupCount = Integer.parseInt(countString);
        }catch (NumberFormatException e){
            Log.i(TAG, "inputCount: error parse count string "+countString);
        }
        if(!countString.equals(""+count)) {
            if(isValidCount(setupCount)){
                count=setupCount;
            }
            countEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    countEditText.setText(""+count);
                }
            },3000);
        }
    }

    protected int fixedByDefaultCount(int count){
        if(count<defaultCount){
            count=defaultCount;
        }
        return count;
    }

    protected boolean isValidCount(int count){
        return minCount<=count && count<=maxCount;
    }

    protected void incCount(){
        count++;
        if(count > maxCount){
            count=maxCount;
        }
        countEditText.setText(""+count);
    }

    protected void decCount(){
        count--;
        if(count < minCount){
            count=minCount;
        }else if(count < defaultCount){
            count=defaultCount;
        }
        countEditText.setText(""+count);
    }

    public int getCount() {
        return count;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        minCount=fixedByDefaultCount(minCount);
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        maxCount=fixedByDefaultCount(maxCount);
        this.maxCount = maxCount;
    }
}
