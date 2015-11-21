/*
* vwd KL
* Created by zzhao on 11/19/15 9:34 AM
*/
package com.example.component

import spock.lang.Specification

/**
 * @author zzhao
 */
class ObservableQueueSpec extends Specification {

    def "observer based observable queue should work"() {
        given:
        def ooq = new ObservableQueue<Integer>()
        def result = []
        ooq.observe().subscribe { i -> result << i }

        when:
        ooq.offer(1)
        ooq.offer(2)

        then:
        "[1, 2]".equals(result.toString())
    }

    def "subject based observable queue should work"() {
        given:
        def ooq = new SubjectQueue<Integer>()
        def result = []
        ooq.observe().subscribe { i -> result << i }

        when:
        ooq.offer(1)
        ooq.offer(2)

        then:
        "[1, 2]".equals(result.toString())
    }
}
