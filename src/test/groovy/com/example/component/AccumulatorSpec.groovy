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
    long A = 1

    @Shared
    long B = 2

    @Shared
    long C = 3

    @Shared
    long D = -4

    @Shared
    long INITIAL = 0L

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

    @SuppressWarnings("all")
    def 'should accumulate numbers using operator'() {
        given:
        def accumulator = new LongAccumulator(operator, initial)

        when:
        accumulator.accumulate(A)
        accumulator.accumulate(B)
        accumulator.accumulate(C)
        accumulator.accumulate(D)
        then:
        accumulator.get() == result

        where:
        operator | initial | result
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
