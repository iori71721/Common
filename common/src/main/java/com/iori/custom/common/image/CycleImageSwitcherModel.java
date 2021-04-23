package com.iori.custom.common.image;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageSwitcher;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * usage:
 * 1.constructor
 * 2.must setup in animation
 * 3.setup {@link #imageSource},then call {@link #start()} to cycle play image
 * 4.when change {@link #imageSource} will call {@link #stop()}
 *
 * @param <T> image source,like List<String> or List<Bitmap> ...
 */
public abstract class CycleImageSwitcherModel <T extends Collection>{
    private T imageSource;
    private ImageSwitcher imageSwitcher;
    private int playTimeByMs=1000;
    private boolean start;
    private int playIndex;
    private int lastPlayIndex;
    private CycleImageSwitcherModelCallback cycleImageSwitcherModelCallback;

    public CycleImageSwitcherModel(@NonNull T imageSource,@NonNull ImageSwitcher imageSwitcher) {
        this.imageSource = imageSource;
        this.imageSwitcher = imageSwitcher;
        init();
    }

    protected void init(){
        imageSwitcher.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageSwitcher.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cyclePlayImage();
                    }
                },playTimeByMs);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void reset(){
        if(!start) {
            playIndex = 0;
            lastPlayIndex=0;
        }
    }

    public void start(){
        if(!start){
            start=true;
            cyclePlayImage();
            if(cycleImageSwitcherModelCallback != null){
                cycleImageSwitcherModelCallback.start();
            }
        }
    }

    private int fixedPlayIndex(int playIndex){
        synchronized (imageSource) {
            return playIndex <= imageSource.size() - 1 ? playIndex : 0;
        }
    }

    private void cyclePlayImage(){
        if(start){
            lastPlayIndex=playIndex;
            Drawable playDrawable=fetchNextImage(imageSource,playIndex);
            imageSwitcher.setImageDrawable(playDrawable);
            if(cycleImageSwitcherModelCallback != null){
                cycleImageSwitcherModelCallback.play(playIndex);
            }
            playIndex++;
            playIndex=fixedPlayIndex(playIndex);
        }
    }

    protected abstract Drawable fetchNextImage(T imageSource,int currentIndex);

    public void stop(){
        if(start){
            start=false;
            if(cycleImageSwitcherModelCallback != null){
                cycleImageSwitcherModelCallback.stop(lastPlayIndex);
            }
        }
    }

    public @NonNull T getImageSource() {
        return imageSource;
    }

    public void setImageSource(@NonNull T imageSource) {
        if(start){
            return;
        }
        this.imageSource = imageSource;
    }

    public ImageSwitcher getImageSwitcher() {
        return imageSwitcher;
    }

    public void setImageSwitcher(ImageSwitcher imageSwitcher) {
        this.imageSwitcher = imageSwitcher;
    }

    public int getPlayTimeByMs() {
        return playTimeByMs;
    }

    public void setPlayTimeByMs(int playTimeByMs) {
        this.playTimeByMs = playTimeByMs;
    }

    public CycleImageSwitcherModelCallback getCycleImageSwitcherModelCallback() {
        return cycleImageSwitcherModelCallback;
    }

    public void setCycleImageSwitcherModelCallback(CycleImageSwitcherModelCallback cycleImageSwitcherModelCallback) {
        this.cycleImageSwitcherModelCallback = cycleImageSwitcherModelCallback;
    }

    public boolean isStart() {
        return start;
    }

    public int getPlayIndex() {
        return playIndex;
    }

    public int getLastPlayIndex() {
        return lastPlayIndex;
    }

    public static interface CycleImageSwitcherModelCallback{
        void start();
        void play(int playIndex);
        void stop(int stopIndex);
    }
}
