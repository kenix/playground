package com.example.component

import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.LongAccumulator
import java.util.stream.LongStream

/**
 * @author zzhao
 */
class AccumulatorSpec extends Specification {

    @Shared
    long a = 1

    @Shared
    long b = 2

    @Shared
    long c = 3

    @Shared
    long d = -4

    @Shared
    long initial = 0L

    def 'should add few numbers'() {
        given:
        def accumulator = new LongAccumulator({ long x, long y -> x + y }, initial)

        when:
        accumulator.accumulate(a)
        accumulator.accumulate(b)
        accumulator.accumulate(c)
        accumulator.accumulate(d)
        then:
        accumulator.get() == initial + a + b + c + d
    }

    @SuppressWarnings("all")
    def 'should accumulate numbers using operator'() {
        given:
        def accumulator = new LongAccumulator(operator, initial)

        when:
        accumulator.accumulate(a)
        accumulator.accumulate(b)
        accumulator.accumulate(c)
        accumulator.accumulate(d)
        then:
        accumulator.get() == result

        where:
        operator | initial | result
                { x, y -> x + y } | 0 | a + b + c + d
                { x, y -> x * y } | 1 | a * b * c * d
                { x, y -> Math.max(x, y) } | Integer.MIN_VALUE | max(a, b, c, d)
                { x, y -> Math.min(x, y) } | Integer.MAX_VALUE | min(a, b, c, d)
    }

    def max(long ... nums) {
        return LongStream.of(nums).max().getAsLong();
    }

    def min(long ... nums) {
        return LongStream.of(nums).min().getAsLong();
    }
}
