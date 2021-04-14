package com.iori.custom.common.tools;

import java.util.Collection;

public class NonEmptyTool {
    /**
     * @param originalCollection
     * @param emptyCollectionClass
     * @param <T> original collection class type
     * @param <C> generate empty collection class type
     * @return
     */
    public static <T extends Collection,C extends Collection> T getNonEmptyCollection(T originalCollection, Class<C> emptyCollectionClass){
        if(originalCollection == null){
            try {
                originalCollection=(T)emptyCollectionClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return originalCollection;
    }
}
