package com.iori.custom.common.adapter.spinner;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.iori.custom.common.adapter.CommonBaseArrayAdapter;

import java.util.List;

/**
 * usage:
 * 1.setup last index to hint text
 * 2.override {@link #initViewLayout(Object, Object, int)}
 * @param <V>
 * @param <DropV>
 * @param <D>
 */
public abstract class BaseHintSpinnerAdapter<V, DropV, D> extends CommonBaseArrayAdapter<V, DropV, D> {
    public BaseHintSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public BaseHintSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public BaseHintSpinnerAdapter(@NonNull Context context, int resource, @NonNull D[] objects) {
        super(context, resource, objects);
    }

    public BaseHintSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull D[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public BaseHintSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<D> objects) {
        super(context, resource, objects);
    }

    public BaseHintSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<D> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public final int getHintIndex(){
        return getCount();
    }

    @Override
    @CallSuper
    protected void initViewLayout(V v, D d, int position) {
        if(position == getHintIndex()){
            initHintView(position,v,d);
        }else{
            initSelectView(position,v,d);
        }
    }

    protected abstract void initSelectView(int position, V viewHolder, D itemData);

    protected abstract void initHintView(int position, V viewHolder, D itemData);

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

    public boolean isSelectItem(int currentIndex){
        return !(currentIndex==getHintIndex());
    }
}
