package com.example.component

import spock.lang.Specification

import java.util.concurrent.atomic.LongAccumulator
import java.util.stream.LongStream

/**
 * @author zzhao
 */
class AccumulatorSpec extends Specification {

    public static final long A = 1
    public static final long B = 2
    public static final long C = 3
    public static final long D = -4
    public static final long INITIAL = 0L

    def 'should add few numbers'() {
        given:
        def accumulator = new LongAccumulator({ long x, long y -> x + y }, INITIAL)
        when:
        accumulator.accumulate(A)
        accumulator.accumulate(B)
        accumulator.accumulate(C)
        accumulator.accumulate(D)
        then:
        accumulator.get() == INITIAL + A + B + C + D
    }

    def 'should accumulate numbers using operator'() {
        given:
        def accumulator = new LongAccumulator(operator, initial)
        when:
        accumulator.accumulate(A)
        accumulator.accumulate(B)
        accumulator.accumulate(C)
        accumulator.accumulate(D)
        then:
        accumulator.get() == expected
        where:
        operator | initial | expected
                { x, y -> x + y } | 0 | A + B + C + D
                { x, y -> x * y } | 1 | A * B * C * D
                { x, y -> Math.max(x, y) } | Integer.MIN_VALUE | max(A, B, C, D)
                { x, y -> Math.min(x, y) } | Integer.MAX_VALUE | min(A, B, C, D)
    }

    def max(long ... nums) {
        return LongStream.of(nums).max().getAsLong();
    }

    def min(long ... nums) {
        return LongStream.of(nums).min().getAsLong();
    }
}
