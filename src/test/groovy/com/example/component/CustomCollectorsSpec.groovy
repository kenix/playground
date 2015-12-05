package com.example.component

import spock.lang.Specification
import spock.lang.Unroll

import static CustomCollectors.sliding;

/**
 * Copied from <a href="http://www.nurkiewicz.com/2014/07/grouping-sampling-and-batching-custom.html?view=sidebar">
 *     NoBlogDefFound</a>.
 *
 * @author zzhao
 */
@Unroll
@SuppressWarnings("all")
class CustomCollectorsSpec extends Specification {

    def "Sliding window of #input with size #size and step of 1 is #output"() {
        expect:
        input.stream().collect(sliding(size)) == output

        where:
        input  | size | output
        []     | 5    | []
        [1]    | 1    | [[1]]
        [1, 2] | 1    | [[1], [2]]
        [1, 2] | 2    | [[1, 2]]
        [1, 2] | 3    | [[1, 2]]
        1..3   | 3    | [[1, 2, 3]]
        1..4   | 2    | [[1, 2], [2, 3], [3, 4]]
        1..4   | 3    | [[1, 2, 3], [2, 3, 4]]
        1..7   | 3    | [[1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6], [5, 6, 7]]
        1..7   | 6    | [1..6, 2..7]
    }

    def "Sliding window of #input with size #size and no overlapping is #output"() {
        expect:
        input.stream().collect(sliding(size, size)) == output

        where:
        input | size | output
        []    | 5    | []
        1..3  | 2    | [[1, 2], [3]]
        1..4  | 4    | [1..4]
        1..4  | 5    | [1..4]
        1..7  | 3    | [1..3, 4..6, [7]]
        1..6  | 2    | [[1, 2], [3, 4], [5, 6]]
    }

    def "Sliding window of #input with size #size and some overlapping is #output"() {
        expect:
        input.stream().collect(sliding(size, 2)) == output

        where:
        input | size | output
        []    | 5    | []
        1..4  | 5    | [[1, 2, 3, 4]]
        1..7  | 3    | [1..3, 3..5, 5..7]
        1..6  | 4    | [1..4, 3..6]
        1..9  | 4    | [1..4, 3..6, 5..8, 7..9]
        1..10 | 4    | [1..4, 3..6, 5..8, 7..10]
        1..11 | 4    | [1..4, 3..6, 5..8, 7..10, 9..11]
    }

    def "Sliding window of #input with size #size and gap of #gap is #output"() {
        expect:
        input.stream().collect(sliding(size, size + gap)) == output

        where:
        input | size | gap | output
        []    | 5    | 1   | []
        1..9  | 4    | 2   | [1..4, 7..9]
        1..10 | 4    | 2   | [1..4, 7..10]
        1..11 | 4    | 2   | [1..4, 7..10]
        1..12 | 4    | 2   | [1..4, 7..10]
        1..13 | 4    | 2   | [1..4, 7..10, [13]]
        1..13 | 5    | 1   | [1..5, 7..11, [13]]
        1..12 | 5    | 3   | [1..5, 9..12]
        1..13 | 5    | 3   | [1..5, 9..13]
    }

    def "Sampling #input taking every #nth th element is #output"() {
        expect:
        input.stream().collect(sliding(1, nth)) == output

        where:
        input  | nth | output
        []     | 1   | []
        []     | 5   | []
        1..3   | 5   | [[1]]
        1..6   | 2   | [[1], [3], [5]]
        1..10  | 5   | [[1], [6]]
        1..100 | 30  | [[1], [31], [61], [91]]
    }
}
