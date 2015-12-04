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
class StreamUtilSpec extends Specification {

    @Unroll
    def "#type #elements reverse streaming: #output"() {
        expect:
        StreamUtil.reverse(elements).collect(Collectors.toList()) == output

        where:
        type         | elements                | output
        'Array'      | [1, 2, 3].toArray()     | [3, 2, 1]
        'LinkedList' | [1, 2, 3] as LinkedList | [3, 2, 1]
        'List'       | [1, 2, 3]               | [3, 2, 1]
    }
}
