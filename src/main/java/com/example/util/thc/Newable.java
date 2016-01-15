/*
* vwd KL
* Created by zzhao on 1/15/16 12:54 PM
*/
package com.example.util.thc;

/**
 */
public interface Newable<T> extends MethodFinder {
    default Class<T> type() {
        return (Class<T>) parameter(0).getType();
    }

    default T newInstance() {
        try {
            return type().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
