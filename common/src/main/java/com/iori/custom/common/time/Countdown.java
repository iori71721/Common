package com.iori.custom.common.time;

import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Countdown {
    private boolean start;

    private int sec;

    private int remainingSec;

    private int countdownIntervalSec=1;
    
    private CountdownListener countdownListener;

    private Timer timer=new Timer();

    public void start(){
        if(!start){
            start=true;
            if(sec > 0){
                remainingSec=sec;
                if(countdownListener != null){
                    countdownListener.start();
                }
                timer.schedule(new CountdownTask(),countdownIntervalSec*1000, countdownIntervalSec*1000);
            }
        }
    }

    public void stop(){
        if(start){
            start=false;
            timer.cancel();
            readyNewTimer();
            if(countdownListener != null){
                countdownListener.stop(remainingSec);
            }
        }
    }

    private void readyNewTimer() {
        timer=new Timer();
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        if(canSetup()) {
            if(sec < 0){
                sec=0;
            }
            this.sec = sec;
        }
    }

    private boolean canSetup(){
        return !start;
    }

    /**
     * example:input 3600 sec,calc type is hour,will return 1
     * @param calcSec
     * @param type second or hour etc.
     * @return value for type
     */
    public static float calcTime(int calcSec,CalcTimeType type){
        float result=0;
        if(calcSec <0 ){
            calcSec=0;
        }
        switch (type){
            case second:
            case minute:
            case hour:
            case day:
                result=1.0f*calcSec/type.getCalcBaseSec();
                break;
        }
        return result;
    }

    private static int calcTimeForInteger(int calcSec,CalcTimeType type){
        int result=0;
        if(calcSec <0 ){
            calcSec=0;
        }
        switch (type){
            case second:
                result=calcSec%CalcTimeType.minute.getCalcBaseSec();
                break;
            case minute:
            case hour:
            case day:
                result=calcSec/type.getCalcBaseSec();
                break;
        }
        return result;
    }

    public static @NonNull List<DisplayTime> calcDisplayTime(int calcSec){
        List<DisplayTime> displayTimeList=new ArrayList<>(5);

        int remainingSec=calcSec;
        CalcTimeType type=CalcTimeType.day;
        remainingSec=addDisplayTimeAndCalcRemainingSec(displayTimeList,type,remainingSec);

        type=CalcTimeType.hour;
        remainingSec=addDisplayTimeAndCalcRemainingSec(displayTimeList,type,remainingSec);

        type=CalcTimeType.minute;
        remainingSec=addDisplayTimeAndCalcRemainingSec(displayTimeList,type,remainingSec);

        type=CalcTimeType.second;
        remainingSec=addDisplayTimeAndCalcRemainingSec(displayTimeList,type,remainingSec);

        return displayTimeList;
    }

    private static int addDisplayTimeAndCalcRemainingSec(@NonNull List<DisplayTime> displayTimes,CalcTimeType type,int beforeRemainingSec){
        Point calcResult;
        calcResult=calcTimeForIntegerAndRemainingSec(beforeRemainingSec,type);
        DisplayTime addDisplayTime=new DisplayTime(type,calcResult.x);

        if(addDisplayTime.value > 0){
            displayTimes.add(addDisplayTime);
        }

        return calcResult.y;
    }

    /**
     *
     * @param beforeRemainingSec
     * @param calcTimeType
     * @return X is calcTimeForInteger see {@link #calcTimeForInteger(int, CalcTimeType)},Y is remaining sec after calc.
     */
    private static Point calcTimeForIntegerAndRemainingSec(int beforeRemainingSec,CalcTimeType calcTimeType){
        Point result=new Point();
        result.x=calcTimeForInteger(beforeRemainingSec,calcTimeType);
        result.y=beforeRemainingSec-calcTimeType.getCalcBaseSec()*result.x;
        return result;
    }

    public int getCountdownIntervalSec() {
        return countdownIntervalSec;
    }

    public void setCountdownIntervalSec(int countdownIntervalSec) {
        if(canSetup()) {
            if(countdownIntervalSec < 0){
                countdownIntervalSec =0;
            }
            this.countdownIntervalSec = countdownIntervalSec;
        }
    }

    public CountdownListener getCountdownListener() {
        return countdownListener;
    }

    public void setCountdownListener(CountdownListener countdownListener) {
        this.countdownListener = countdownListener;
    }

    public boolean isStart() {
        return start;
    }

    public int getRemainingSec() {
        return remainingSec;
    }

    private class CountdownTask extends TimerTask{

        @Override
        public void run() {
            remainingSec-= countdownIntervalSec;
            if(remainingSec <=0 ){
                remainingSec=0;
                stop();
            }else{
                if(countdownListener != null){
                    countdownListener.update(remainingSec);
                }
            }
        }
    }
    
    public static interface CountdownListener{
        void start();
        void stop(int remainingSec);
        void update(int remainingSec);
    }

    public static enum CalcTimeType{
        second(1)
        ,minute(60)
        ,hour(60*60)
        ,day(60*60*24);
        private final int calcBaseSec;

        CalcTimeType(int calcBaseSec) {
            this.calcBaseSec = calcBaseSec;
        }

        public int getCalcBaseSec() {
            return calcBaseSec;
        }
    }

    public static class DisplayTime{
        private final CalcTimeType Type;
        private final int value;

        public DisplayTime(CalcTimeType type, int value) {
            Type = type;
            this.value = value;
        }

        public CalcTimeType getType() {
            return Type;
        }

        public int getValue() {
            return value;
        }
    }
}
