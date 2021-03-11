package com.iori.custom.common.database;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @param <C> database connection
 * @param <L> local data
 * @param <S> server data
 * @param <P> data primary key type
 */
public abstract class DBUpdateFromServer <C,L,S,P>{
    public void updateFromServer(@NonNull C connection, L localData, S serverData){
        if(serverData == null){
            return;
        }
        L serverConvertToLocal=convertLocalData(serverData);
        delInvalidLocalData(connection,localData,serverConvertToLocal);
    }

    protected abstract @NonNull L convertLocalData(@NonNull S serverData);

    protected abstract boolean isServerDataEmpty(@NonNull L serverData);

    protected abstract void loadLocalDataKeys(@NonNull Set<P> localDataKeys, L localData);

    protected abstract void loadServerDataKeys(@NonNull Set<P> serverDataKeys,@NonNull L serverData);

    protected abstract void executeDelInvalidLocalData(@NonNull C connection,L localData,@NonNull Set<P> localRemoveDataKeys);

    protected abstract void updateServerData(@NonNull C connection,@NonNull L serverData);

    protected void delInvalidLocalData(@NonNull C connection,L localData,@NonNull L serverData){
        if(isServerDataEmpty(serverData)){
            return;
        }
        Set<P> localDataKeys;
        Set<P> serverDataKeys;
        if(serverData instanceof Collection){
            localDataKeys=new HashSet<>(((Collection) serverData).size());
            serverDataKeys=new HashSet<>(((Collection) serverData).size());
        }else{
            localDataKeys=new HashSet<>();
            serverDataKeys=new HashSet<>();
        }
        loadLocalDataKeys(localDataKeys,localData);
        loadServerDataKeys(serverDataKeys,serverData);

        Set<P> willRemoveLocalDataKeys=new HashSet<>(localDataKeys.size());
        for(P localDataKey:localDataKeys){
            if(!serverDataKeys.contains(localDataKey)){
                willRemoveLocalDataKeys.add(localDataKey);
            }
        }

        executeDelInvalidLocalData(connection,localData,willRemoveLocalDataKeys);
        updateServerData(connection,serverData);
    }
}
