/*
* vwd KL
* Created by zzhao on 12/4/15 5:43 PM
*/
package com.example.util

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors;

/**
 * @author zzhao
 */
@Unroll
class StreamUtilSpec extends Specification {

    def "#type #elements reverse streaming: #output"() {
        expect:
        StreamUtil.reverse(elements).collect(Collectors.toList()) == output

        where:
        type         | elements                | output
        'Array'      | [1, 2, 3].toArray()     | [3, 2, 1]
        'LinkedList' | [1, 2, 3] as LinkedList | [3, 2, 1]
        'List'       | [1, 2, 3]               | [3, 2, 1]
    }

    def "primes range from #from to #to: #primes"() {
        expect:
        Arrays.asList(StreamUtil
                .primeRange(from, to)
                .sorted()
                .toArray()) == primes

        where:
        from        | to          | primes
        1           | 10          | [2, 3, 5, 7]
        982_451_652 | 982_451_654 | [982_451_653]
    }
}
