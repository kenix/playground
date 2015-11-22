/*
* Created at 22:36 on 22/11/15
*/
package com.example.util;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author zzhao
 */
public final class CollectionUtil {

    private CollectionUtil() {
        throw new AssertionError("not for instantiation or inheritance");
    }

    public static <T> T last(List<T> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(list.size() - 1);
    }
}
