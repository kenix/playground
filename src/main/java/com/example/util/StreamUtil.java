/*
* vwd KL
* Created by zzhao on 12/4/15 5:34 PM
*/
package com.example.util;

import java.util.*;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author zzhao
 */
public final class StreamUtil {

    private StreamUtil() {
        throw new AssertionError("not for instantiation or inheritance");
    }

    public static <T> Stream<T> reverse(T[] array) {
        final int len = array.length;
        return IntStream.rangeClosed(1, len).mapToObj(i -> array[len - i]);
    }

    private static <T> Stream<T> reverse(LinkedList<T> list) {
        final Iterator<T> it = list.descendingIterator();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
    }

    public static <T> Stream<T> reverse(List<T> list) {
        if (list instanceof LinkedList<?>) {
            return reverse((LinkedList<T>) list);
        }

        final int size = list.size();
        return IntStream.rangeClosed(1, size).mapToObj(i -> list.get(size - i));
    }

    public static LongPredicate IS_PRIME = x -> x > 1 && !LongStream
            .rangeClosed(2, Math.round(Math.floor(Math.sqrt(x))))
            .parallel()
            .filter(i -> x % i == 0)
            .findAny()
            .isPresent();

    public static LongStream primeRange(long from, long to) {
        return LongStream
                .range(from, to)
                .parallel()
                .filter(IS_PRIME);
    }
}
