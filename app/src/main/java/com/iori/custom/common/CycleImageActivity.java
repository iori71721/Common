package com.iori.custom.common;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.image.CycleImageSwitcherModel;

import java.util.ArrayList;
import java.util.List;

public class CycleImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageSwitcher imageSwitcher;
    private CycleImageSwitcherModel<List<Integer>> cycleImageSwitcherModel;
    private List<Integer> imageIDs=new ArrayList<>(100);
    private Button start;
    private Button stop;
    private Button reset;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_image_activity);

        imageSwitcher=findViewById(R.id.imageSwitcher);
        start=findViewById(R.id.start);
        stop=findViewById(R.id.stop);
        reset=findViewById(R.id.reset);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        reset.setOnClickListener(this);

        initImageSwitcher();
    }

    private void initImageSwitcher() {
        int animationMs=2000;
        imageSwitcher.getInAnimation().setDuration(animationMs);
        imageSwitcher.getOutAnimation().setDuration(animationMs);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(CycleImageActivity.this);
                FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                return imageView;
            }
        });

        imageIDs.add(R.drawable.user);
        imageIDs.add(R.drawable.server);
        imageIDs.add(R.drawable.password);
        imageIDs.add(R.drawable.start_stream);

        cycleImageSwitcherModel=new CycleImageSwitcherModel<List<Integer>>(imageIDs,imageSwitcher) {
            @Override
            protected Drawable fetchNextImage(List<Integer> imageSource, int currentIndex) {
                return getDrawable(imageSource.get(currentIndex));
            }
        };
        
        cycleImageSwitcherModel.setCycleImageSwitcherModelCallback(new CycleImageSwitcherModel.CycleImageSwitcherModelCallback() {
            @Override
            public void start() {
                Log.d("iori_cycleImage", "start: ");
            }

            @Override
            public void play(int playIndex) {
                Log.d("iori_cycleImage", "play: playIndex "+playIndex);
            }

            @Override
            public void stop(int stopIndex) {
                Log.d("iori_cycleImage", "stop: stopIndex "+stopIndex);
            }
        });
    }

    @Override
    protected void onDestroy() {
        cycleImageSwitcherModel.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:
                cycleImageSwitcherModel.start();
                break;
            case R.id.stop:
                cycleImageSwitcherModel.stop();
                break;
            case R.id.reset:
                cycleImageSwitcherModel.reset();
                break;
        }
    }
}
