package com.iori.custom.common.reflect;

import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public class ReflectHelper {
    private static final String TAG=ReflectHelper.class.getName();

    public static <T> void setValue(Object setObject,Class setupClass,String attrName, Object setValue,ReflectParser parser){
        Field setupField;
        try {
            setupField=setupClass.getDeclaredField(attrName);
            setupField.setAccessible(true);
            setupField.set(setObject,parser.parse(setValue));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "setValue: fail attrName "+attrName+" setup class "+setupClass);
            e.printStackTrace();
        }
    }

    public static @Nullable <T> T getValue(Object getObject, Class getObjectClass, String attrName, Class<T> attrClass){
        Field getField;
        T getValue=null;
        try {
            getField=getObjectClass.getDeclaredField(attrName);
            getField.setAccessible(true);
            getValue=attrClass.cast(getField.get(getObject));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "getValue: fail attrName "+attrName+" get class "+getObjectClass);
            e.printStackTrace();
        }
        return getValue;
    }

    public static interface ReflectParser<T>{
        T parse(Object parseObject);
    }
}
