package com.iori.custom.common.database.spinner;

import androidx.annotation.NonNull;

import java.util.List;

/**
 *
 * @param <C> db connection
 * @param <D> table type
 */
public abstract class SpinnerDataOperation<C,D> {
    public List<D> getAtLeastOneTables(C connection){
        List<D> atLeastOneTables=queryTables(connection);
        if(atLeastOneTables.size() == 0){
            atLeastOneTables.add(generateTempTable(connection));
        }
        return atLeastOneTables;
    }

    protected abstract @NonNull
    List<D> queryTables(C connection);

    protected abstract D generateTempTable(C connection);
}
