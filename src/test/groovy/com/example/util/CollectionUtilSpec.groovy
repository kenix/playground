package com.example.util

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author zzhao
 */
@Unroll
class CollectionUtilSpec extends Specification {

  def 'interleaving iterate over #its produces #result'() {
    when:
    def it = CollectionUtil.interleavingIterator(its.collect { it.iterator() })
    then:
    it.toList() == result

    where:
    its                                | result
    [['ia', 'ib', 'ic'], ['ja', 'jb']] | ['ia', 'ja', 'ib', 'jb', 'ic']
    [[1, 2, 3], [4, 5], [6, 7, 8, 9]]  | [1, 4, 6, 2, 5, 7, 3, 8, 9]
  }
}
