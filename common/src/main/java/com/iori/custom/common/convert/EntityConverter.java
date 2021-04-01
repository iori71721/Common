package com.iori.custom.common.convert;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <D> entity convert to local data type
 * @param <E> entity type
 */
public abstract class EntityConverter<D,E> {
    protected abstract @NonNull
    D createLocalData();
    protected abstract void importEntityToLocalData(@NonNull D localData, @NonNull E importEntity);

    public D convertLocalData(E entity){
        D convetData=null;
        if(entity != null){
            convetData=createLocalData();
            importEntityToLocalData(convetData,entity);
        }
        return convetData;
    }

    public @NonNull
    List<D> convertLocalDatas(List<E> importEntities){
        List<D> convetDatas=null;
        if(importEntities == null){
            convetDatas=new ArrayList<>();
        }else{
            convetDatas=new ArrayList<>(importEntities.size());
            D convertData;
            for(E convertEntity:importEntities){
                convertData=convertLocalData(convertEntity);
                if(convertData != null){
                    convetDatas.add(convertData);
                }
            }
        }
        return convetDatas;
    }

    protected abstract @NonNull E createEntity();
    protected abstract void importLocalDataToEntity(@NonNull E entity,@NonNull D importLoadData);

    public E convertEntity(D localData){
        E entity=null;
        if(localData != null){
            entity=createEntity();
            importLocalDataToEntity(entity,localData);
        }
        return entity;
    }

    public @NonNull List<E> convertEntities(List<D> localDatas){
        List<E> entities=null;
        if(localDatas != null){
            entities=new ArrayList<>(localDatas.size());
            E convertEntity=null;
            for(D localData:localDatas){
                convertEntity=convertEntity(localData);
                if(convertEntity != null){
                    entities.add(convertEntity);
                }
            }
        }else{
            entities=new ArrayList<>();
        }
        return entities;
    }
}
