/*
* Created at 00:41 on 19/11/15
*/
package com.example.component;

import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author zzhao
 */
public class ImmutableSetCollector<T> implements Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> {

    public static <T> Collector<T, ?, ImmutableSet<T>> create() {
        return new ImmutableSetCollector<>();
    }

    @Override
    public Supplier<ImmutableSet.Builder<T>> supplier() {
        return ImmutableSet::builder;
    }

    @Override
    public BiConsumer<ImmutableSet.Builder<T>, T> accumulator() {
        return ImmutableSet.Builder::add;
    }

    @Override
    public BinaryOperator<ImmutableSet.Builder<T>> combiner() {
        return (left, right) -> left.addAll(right.build());
    }

    @Override
    public Function<ImmutableSet.Builder<T>, ImmutableSet<T>> finisher() {
        return ImmutableSet.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
