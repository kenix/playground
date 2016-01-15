/*
* vwd KL
* Created by zzhao on 1/15/16 12:57 PM
*/
package com.example.util.thc;

import java.util.function.Consumer;

/**
 */
public interface TypeReference<T> extends Newable<T> {

    T typeIs(T i);

    default Consumer<T> consumer() {
        return this::typeIs;
    }
}
