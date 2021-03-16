package com.iori.custom.common.database;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * usage:
 * 1.if data is empty,used empty collection
 * @param <C> database connection
 * @param <L> local data
 * @param <S> server data
 * @param <P> data primary key type
 */
public abstract class DBUpdateFromServer <C,L extends Collection,S extends Collection,P>{
    public void updateFromServer(@NonNull C connection,@NonNull L localData,@NonNull S serverData){
        L serverConvertToLocal=convertLocalData(serverData);
        delInvalidLocalData(connection,localData,serverConvertToLocal);
        if(!isServerDataEmpty(serverConvertToLocal)) {
            updateServerData(connection, serverConvertToLocal);
        }
    }

    protected abstract @NonNull L convertLocalData(@NonNull S serverData);

    protected boolean isServerDataEmpty(@NonNull L serverData){
        return serverData.isEmpty();
    }

    protected abstract void loadLocalDataKeys(@NonNull Set<P> localDataKeys,@NonNull L localData);

    protected abstract void loadServerDataKeys(@NonNull Set<P> serverDataKeys,@NonNull L serverData);

    protected abstract void executeDelInvalidLocalData(@NonNull C connection,@NonNull L localData,@NonNull Set<P> localRemoveDataKeys);

    protected abstract void updateServerData(@NonNull C connection,@NonNull L serverData);

    protected void delInvalidLocalData(@NonNull C connection,@NonNull L localData,@NonNull L serverData){
        Set<P> localDataKeys;
        Set<P> serverDataKeys;
        localDataKeys=new HashSet<>(localData.size());
        serverDataKeys=new HashSet<>(serverData.size());

        loadLocalDataKeys(localDataKeys,localData);
        loadServerDataKeys(serverDataKeys,serverData);

        Set<P> willRemoveLocalDataKeys=new HashSet<>(localDataKeys.size());
        for(P localDataKey:localDataKeys){
            if(!serverDataKeys.contains(localDataKey)){
                willRemoveLocalDataKeys.add(localDataKey);
            }
        }

        if(!willRemoveLocalDataKeys.isEmpty()) {
            executeDelInvalidLocalData(connection, localData, willRemoveLocalDataKeys);
        }
    }
}
