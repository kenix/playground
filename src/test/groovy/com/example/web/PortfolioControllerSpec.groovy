package com.example.web

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.AbstractSubscribableChannel
import org.springframework.messaging.support.MessageBuilder

import java.nio.charset.Charset
import java.security.Principal

/**
 * @author zzhao
 */
@Slf4j
class PortfolioControllerSpec extends WebIntegrationBaseSpec {

    @Autowired
    AbstractSubscribableChannel clientInboundChannel

    @Autowired
    AbstractSubscribableChannel clientOutboundChannel

    @Autowired
    AbstractSubscribableChannel brokerChannel

    @Autowired
    ObjectMapper objectMapper

    TestChannelInterceptor channelInterceptorClientOutbound

    TestChannelInterceptor channelInterceptorBroker

    def setup() {
        this.channelInterceptorClientOutbound = new TestChannelInterceptor()
        this.channelInterceptorBroker = new TestChannelInterceptor()

        this.clientOutboundChannel.addInterceptor(this.channelInterceptorClientOutbound)
        this.brokerChannel.addInterceptor(this.channelInterceptorBroker)
    }

    def cleanup() {
        this.clientOutboundChannel.removeInterceptor(this.channelInterceptorClientOutbound)
        this.brokerChannel.removeInterceptor(this.channelInterceptorBroker)
    }

    def "all portfolio positions should be subscribable"() {
        given:
        def header = StompHeaderAccessor.create(SimpMessageType.SUBSCRIBE)
        header.subscriptionId = '0'
        header.destination = '/app/all'
        header.sessionId = '0'
        header.user = new Principal() {
            @Override
            String getName() {
                'fabrice'
            }
        }
        header.sessionAttributes = [:]
        def msg = MessageBuilder.createMessage(new byte[0], header.messageHeaders)

        when:
        this.channelInterceptorClientOutbound.setIncludedDestinations('/app/all')
        this.clientInboundChannel.send(msg)
        def reply = this.channelInterceptorClientOutbound.awaitMessage(5)
        then:
        reply

        when:
        def respHeader = StompHeaderAccessor.wrap(reply)
        then:
        respHeader.subscriptionId == '0'
        respHeader.destination == '/app/all'
        respHeader.sessionId == '0'

        when:
        def json = new String((byte[]) reply.getPayload(), Charset.forName("UTF-8"))
        def node = this.objectMapper.readTree(json)
        then:
        node.isArray()
        node.size() == 8
    }
}
