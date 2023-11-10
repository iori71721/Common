package com.iori.custom.common;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.listener.CustomGestureDetector;

public class DropListViewActivity extends AppCompatActivity {
    private ListView dropListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drop_listview_activity);

        dropListView=findViewById(R.id.dropListView);
        initListView();
        enableDrop(dropListView);
    }

    private void initListView() {
        dropListView.setAdapter(new ArrayAdapter<String>(this,R.layout.onetext_item_layout,
            R.id.item_text
            ,new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","x"
                ,"y","z"}));
    }

    private void enableDrop(ListView dropListView) {
        dropListView.setClickable(true);

        final CustomGestureDetector listener=new CustomGestureDetector(dropListView);
        final GestureDetector dragGestureDetector=new GestureDetector(this,listener);

        listener.setEnableLongPressDragMoveMode(true);

        dropListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dragGestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }
}
