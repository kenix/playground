package com.example.util

import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Collectors

/**
 * @author zzhao
 */
class ArrayListSpec extends Specification {

    @Unroll
    '#listImpl basic ops'() {
        given:
        List list = listImpl.newInstance()
        expect:
        list.size() == 0

        when:
        list.add('B')
        list.add('C')
        list.add('D')
        list.add(0, 'A')
        then:
        list.size() == 4
        list == ['A', 'B', 'C', 'D']
        Collections.binarySearch(list, 'A') == 0

        when:
        list.remove(0)
        then:
        list.size() == 3
        list == ['B', 'C', 'D']
        Collections.binarySearch(list, 'C') == 1

        when:
        list.set(1, 'c')
        then:
        Collections.binarySearch(list, 'c') == 1

        where:
        listImpl << [MyArrayList, CircularArrayList, MyArrayList, CircularArrayList]
    }

    @Unroll
    '#listImpl re-capacity'() {
        given:
        List list = listImpl.newInstance()
        expect:
        list.size() == 0

        when:
        1.upto(100) { list.add(it) }
        then:
        list == (1..100).toList()

        when:
        List evenList = list.stream()
                .filter({ it % 2 == 0 })
                .collect(Collectors.toList())
        then:
        evenList.size() == 50
        evenList.every { it % 2 == 0 }

        where:
        listImpl << [MyArrayList, CircularArrayList, MyArrayList, CircularArrayList]
    }
}
