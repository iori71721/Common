package com.iori.custom.common.marquee;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * useage:
 * 1.create object
 * 2.calc marquee width and setup
 * 3.setup moveStep
 * 4.call {@link #start()}
 * 5.cycle call {@link #move()} to implements marquee
 */
public class HorizontalMarquee {
    public static final int MOVE_LEFT_FROM_RIGHT =0;
    public static final int MOVE_RIGHT_FROM_LEFT =1;

    private final int inValidPosition=Integer.MIN_VALUE;
    public int startPosition;
    public int endPosition;
    private int beforePosition=inValidPosition;
    private int currentPosition=inValidPosition;
    private final @MoveType int moveType;
    public int moveStep;
    public int marqueeWidth=0;
    private final MoveBehavior moveBehavior;
    private boolean stop=true;

    public HorizontalMarquee(@MoveType int moveType, int startPosition, int endPosition, int marqueeWidth, MoveBehavior moveBehavior) {
        this.moveType = moveType;
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.marqueeWidth=marqueeWidth;
        this.moveBehavior=moveBehavior;
    }

    private void setupMvoeStartPosition(){
        if(currentPosition == inValidPosition){
            currentPosition=calcInitPosition(moveType,marqueeWidth);
            beforePosition=currentPosition;
        }
    }

    private int calcInitPosition(@MoveType int moveType,int marqueeWidth){
        int initPosition=0;
        switch (moveType){
            case MOVE_RIGHT_FROM_LEFT:
                initPosition=startPosition-marqueeWidth;
                break;
            case MOVE_LEFT_FROM_RIGHT:
                initPosition=endPosition;
                break;
        }
        return initPosition;
    }

    private int calcEndPosition(@MoveType int moveType,int marqueeWidth){
        int endPosition=0;
        switch (moveType){
            case MOVE_RIGHT_FROM_LEFT:
                endPosition=this.endPosition;
                break;
            case MOVE_LEFT_FROM_RIGHT:
                endPosition=startPosition-marqueeWidth;
                break;
        }
        return endPosition;
    }

    private void fixedCurrentPosition(@MoveType int moveType,int marqueeWidth){
        int endPosition=calcEndPosition(moveType,marqueeWidth);
        switch (moveType){
            case MOVE_RIGHT_FROM_LEFT:
                if(currentPosition > endPosition){
                    currentPosition=endPosition;
                }
                break;
            case MOVE_LEFT_FROM_RIGHT:
                if(currentPosition < endPosition){
                    currentPosition=endPosition;
                }
                break;
        }
    }

    private void prepareMove(@MoveType int moveType,int marqueeWidth){
        int endPosition=calcEndPosition(moveType, marqueeWidth);
        beforePosition=currentPosition;
        if(currentPosition == endPosition){
            currentPosition=calcInitPosition(moveType,marqueeWidth);
        }
    }

    private void move(@MoveType int moveType,int moveStep){
        switch (moveType){
            case MOVE_RIGHT_FROM_LEFT:
                currentPosition+=moveStep;
                break;
            case MOVE_LEFT_FROM_RIGHT:
                currentPosition-=moveStep;
                break;
        }
    }

    public void move(){
        setupMvoeStartPosition();
        prepareMove(moveType,marqueeWidth);
        move(moveType,moveStep);
        fixedCurrentPosition(moveType,marqueeWidth);
        if(moveBehavior != null){
            if(!stop) {
                moveBehavior.move(beforePosition, currentPosition);
            }
        }
    }

    public void start(){
        stop=false;
    }

    public void stop(){
        stop=true;
        currentPosition=inValidPosition;
        beforePosition=inValidPosition;
    }

    public boolean isStop() {
        return stop;
    }

    public static interface MoveBehavior{
        void move(int beforePosition,int currentPosition);
    }

    @IntDef({MOVE_LEFT_FROM_RIGHT, MOVE_RIGHT_FROM_LEFT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoveType{}
}
