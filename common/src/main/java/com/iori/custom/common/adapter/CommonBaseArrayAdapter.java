package com.iori.custom.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iori.custom.common.R;

import java.util.List;

/**
 *
 * @param <V> get view holder type
 * @param <DropV> drop down view holder type
 * @param <D> item data
 */
public abstract class CommonBaseArrayAdapter<V,DropV,D> extends ArrayAdapter<D> {

    public CommonBaseArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CommonBaseArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CommonBaseArrayAdapter(@NonNull Context context, int resource, @NonNull D[] objects) {
        super(context, resource, objects);
    }

    public CommonBaseArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull D[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CommonBaseArrayAdapter(@NonNull Context context, int resource, @NonNull List<D> objects) {
        super(context, resource, objects);
    }

    public CommonBaseArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<D> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    protected View generateConvertView(Context context, @LayoutRes int convertViewID) {
        View convertView = View.inflate(context, convertViewID, null);
        return convertView;
    }

    protected View generateDropDownView(Context context, @LayoutRes int convertViewID) {
        View convertView = View.inflate(context, convertViewID, null);
        return convertView;
    }

    protected abstract int getConvertViewID() ;

    protected abstract int getDropDownViewID() ;

    protected abstract V createViewHolder(View view);

    protected abstract DropV createDropDownViewHolder(View view);

    protected abstract void initViewLayout(V viewHolder, D itemData, int position);

    protected abstract void initDropDownViewLayout(DropV viewHolder, D itemData, int position);

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        V viewHolder;
        if (convertView == null) {
            convertView = generateConvertView(getContext(), getConvertViewID());
            viewHolder = createViewHolder(convertView);
            convertView.setTag(R.id.com_iori_custom_common_viewHolder,viewHolder);
        } else {
            viewHolder = (V)convertView.getTag(R.id.com_iori_custom_common_viewHolder);
        }

        D itemData = getItem(position);
        initViewLayout(viewHolder, itemData, position);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DropV viewHolder;
        if (convertView == null) {
            convertView = generateDropDownView(getContext(), getDropDownViewID());
            viewHolder = createDropDownViewHolder(convertView);
            convertView.setTag(R.id.com_iori_custom_common_dropDownViewHolder,viewHolder);
        } else {
            viewHolder = (DropV) convertView.getTag(R.id.com_iori_custom_common_dropDownViewHolder);
        }

        D itemData = getItem(position);

        this.initDropDownViewLayout(viewHolder, itemData, position);
        return convertView;
    }
}
