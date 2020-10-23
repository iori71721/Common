package com.iori.custom.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.LayoutRes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @param <V> view holder type
 * @param <D> item data type
 */
public abstract class CommonBaseAdapter<V,D> extends BaseAdapter {
    protected final Context context;
    protected final List<D> datas=new ArrayList<>(100);

    public CommonBaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public D getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if(convertView == null){
            convertView=generateConvertView(context,getConvertViewID());
            viewHolder=createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(V) convertView.getTag();
        }
        D itemData=getItem(position);
        initLayout(viewHolder,itemData,position);
        return convertView;
    }

    public void addItemData(D itemData){
        synchronized (datas){
            datas.add(itemData);
            notifyDataSetChanged();
        }
    }

    public void addItemDatas(Collection<D> itemDatas){
        synchronized (datas){
            datas.addAll(itemDatas);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        synchronized (datas){
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public void reload(Collection<D> itemDatas){
        synchronized (datas){
            datas.clear();
            addItemDatas(itemDatas);
        }
    }

    protected abstract void initLayout(V viewholder,D itemData,int position);

    protected abstract @LayoutRes
    int getConvertViewID();

    protected abstract V createViewHolder(View convertView);

    protected View generateConvertView(Context context, @LayoutRes int convertViewID){
        View convertView=View.inflate(context,convertViewID,null);
        return convertView;
    }

}
