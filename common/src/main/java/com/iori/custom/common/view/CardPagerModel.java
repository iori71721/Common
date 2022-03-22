package com.iori.custom.common.view;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * usage:
 * 1.create model
 * 2.call {@link #link()}
 */
public class CardPagerModel {
    private RecyclerView cardList;
    private ViewPager2 cardScrollPager;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;

    public CardPagerModel(RecyclerView cardList, ViewPager2 cardScrollPager) {
        this.cardList = cardList;
        this.cardScrollPager = cardScrollPager;
    }

    public void link() {
        cardScrollPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean canScroll;
            private int currentPosition;
            private int lastMovePositionOffsetPixels;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(canScrollList()){
                    int movePixel=positionOffsetPixels-lastMovePositionOffsetPixels;
                    if(positionOffsetPixels == 0){
                        movePixel=0;
                    }

                    if(currentPosition == position) {
                        cardList.scrollBy(movePixel, 0);
                    }else if (currentPosition > position){
                        if(isSkipFirstScrollWhenBackScroll()){
                            cardList.scrollBy(movePixel, 0);
                        }
                    }
                    lastMovePositionOffsetPixels=positionOffsetPixels;
                }
                if(onPageChangeCallback != null){
                    onPageChangeCallback.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            private boolean isSkipFirstScrollWhenBackScroll() {
                return lastMovePositionOffsetPixels != 0;
            }

            private boolean canScrollList() {
                return canScroll;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                canScroll=false;
                fixedCurrentTopViewPosition(position);
                currentPosition= position;
                reset();
                if(onPageChangeCallback != null){
                    onPageChangeCallback.onPageSelected(position);
                }
            }

            private void fixedCurrentTopViewPosition(final int position) {
                View view=cardList.getLayoutManager().findViewByPosition(position);
                if(view != null){
                    view.getLeft();
                    int movePx=view.getLeft()-0;
                    cardList.smoothScrollBy(movePx,0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if(state ==  ViewPager2.SCROLL_STATE_DRAGGING){
                    canScroll=true;
                }else if(state == ViewPager2.SCROLL_STATE_IDLE){
                    fixedCurrentTopViewPosition(currentPosition);
                    reset();
                }
                if(onPageChangeCallback != null){
                    onPageChangeCallback.onPageScrollStateChanged(state);
                }
            }

            private void reset(){
                lastMovePositionOffsetPixels=0;
                canScroll=false;
            }

        });
    }

    public void setOnPageChangeCallback(ViewPager2.OnPageChangeCallback onPageChangeCallback) {
        this.onPageChangeCallback = onPageChangeCallback;
    }
}
