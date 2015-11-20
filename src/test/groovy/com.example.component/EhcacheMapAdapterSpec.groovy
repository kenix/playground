/*
* vwd KL
* Created by zzhao on 11/20/15 4:08 PM
*/
package com.example.component

import net.sf.ehcache.CacheManager
import spock.lang.Specification

/**
 * @author zzhao
 */
class EhcacheMapAdapterSpec extends Specification {

    def "ehcache map adapter should work as a map"() {
        setup:
        def cacheManager = CacheManager.getInstance()
        def testCache = cacheManager.addCacheIfAbsent("test");
        def map = new EhcacheMapAdapter<String, Integer>(testCache)

        when:
        map.one = 1
        map['two'] = 2
        map.put('forty two', 42)
        map.keySet()

        then:
        map.containsKey(k)
        map[k] == v
        thrown(UnsupportedOperationException)

        cleanup:
        cacheManager.clearAll()
        cacheManager.removeCache("test")
        cacheManager.shutdown()

        where:
        k << ['one', 'two', 'forty two']
        v << [1, 2, 42]
    }
}
