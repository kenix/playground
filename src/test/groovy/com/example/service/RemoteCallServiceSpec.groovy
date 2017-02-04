/*
* vwd KL
* Created by zzhao on 3/3/16 3:00 PM
*/
package com.example.service

import com.example.DemoApplication
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author zzhao
 */
@ContextConfiguration(
        loader = SpringBootContextLoader,
        classes = DemoApplication,
        initializers = ConfigFileApplicationContextInitializer
)
@ActiveProfiles("test")
class RemoteCallServiceSpec extends Specification {

    RemoteCallServiceWrapper remoteCallService = new RemoteCallServiceWrapper()

    def idClosure = { args -> args[0] }

    RemoteCallService delegate = Mock() {
        call(_ as String) >> idClosure >> idClosure >> { throw new RuntimeException() } >> idClosure
    }

    def setup() {
        this.remoteCallService.delegate = this.delegate
    }

    def "should call remote service "() {
        when:
        this.remoteCallService.call("test")
        then:
        1 * this.delegate.call("test") >> "test"

        when:
        this.remoteCallService.call("test")
        then:
        1 * this.delegate.call("test") >> "test"

        when:
        this.remoteCallService.call("test")
        then:
        1 * this.delegate.call("test") >> "FALLBACK: test"

        when:
        this.remoteCallService.call("test")
        then:
        1 * this.delegate.call("test") >> "test"
    }
}
