/*
* Created at 20:14 on 22/11/15
*/
package com.example.component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.example.util.CollectionUtil.last;

/**
 * @author zzhao
 */
public final class CustomerCollectors {

    public static <T> Collector<T, ?, List<List<T>>> sliding(int size) {
        return sliding(size, 1);
    }

    public static <T> Collector<T, ?, List<List<T>>> sliding(int size, int step) {
        return new SlidingCollector<>(size, step);
    }

    private static class SlidingCollector<T> implements Collector<T, List<List<T>>, List<List<T>>> {

        private final int size;

        private final int step;

        private int itemIndex;

        private int stepIndex;

        private List<T> nextWindow;

        public SlidingCollector(int size, int step) {
            this.size = size;
            this.step = step;
            this.itemIndex = 0;
            this.stepIndex = 0;
            this.nextWindow = null;
        }

        @Override
        public Supplier<List<List<T>>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<List<T>>, T> accumulator() {
            return (listOfListOfT, t) -> {
                if (needNewWindow(listOfListOfT)) {
                    if (this.nextWindow == null) { // allocate new window
                        this.nextWindow = nextWindow(listOfListOfT);
                    }
                    if (withinWindow()) { // but only used if next item is within window
                        listOfListOfT.add(this.nextWindow);
                        this.nextWindow = null;
                    }
                }
                if (withinWindow()) {
                    last(listOfListOfT).add(t);
                }
                this.itemIndex++;
            };
        }

        private boolean needNewWindow(List<List<T>> listOfListOfT) {
            return listOfListOfT.isEmpty() || last(listOfListOfT).size() == this.size;
        }

        private boolean withinWindow() {
            return this.itemIndex >= this.stepIndex && this.itemIndex < this.stepIndex + this.size;
        }

        private List<T> nextWindow(List<List<T>> listOfListOfT) {
            final List<T> result = new ArrayList<>(this.size);
            if (this.itemIndex == 0) { // first window
                return result;
            }

            this.stepIndex += this.step;
            if (this.stepIndex < this.itemIndex) { // overlap
                fillOverlap(result, listOfListOfT);
            }

            return result;
        }

        private void fillOverlap(List<T> window, List<List<T>> listOfListOfT) {
            final List<T> last = last(listOfListOfT);
            // if overlap, then step index can never be greater than item index
            window.addAll(last.subList(last.size() - (this.itemIndex - this.stepIndex), last.size()));
        }

        @Override
        public BinaryOperator<List<List<T>>> combiner() {
            return (lltLeft, lltRight) -> {
                throw new UnsupportedOperationException();
            };
        }

        @Override
        public Function<List<List<T>>, List<List<T>>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.noneOf(Characteristics.class);
        }
    }
}
