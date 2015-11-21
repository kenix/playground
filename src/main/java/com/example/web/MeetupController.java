/*
* Created at 23:04 on 21/11/15
*/
package com.example.web;

import com.example.api.RxMeetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.schedulers.TimeInterval;

import java.util.concurrent.TimeUnit;

/**
 * @author zzhao
 */
@RestController
@RequestMapping("/meetup")
@Slf4j
public class MeetupController {

    private final RxMeetup meetup;

    private final ObjectMapper objectMapper;

    @Autowired
    public MeetupController(RxMeetup meetup, ObjectMapper objectMapper) {
        this.meetup = meetup;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/{millis}", method = RequestMethod.GET)
    public SseEmitter streamMeetups(@PathVariable short millis) {
        val emitter = new SseEmitter(millis + 500L);
        val sb = new StringBuilder();

        this.meetup.observe()
                .take(millis, TimeUnit.MILLISECONDS)
                .timeInterval().subscribe(
                json -> sendMeetup(json, emitter, sb),
                emitter::completeWithError,
                emitter::complete
        );

        return emitter;
    }

    private void sendMeetup(TimeInterval<String> json, SseEmitter emitter, StringBuilder sb) {
        try {
            val node = this.objectMapper.readTree(json.getValue());
            val utcOffsetMillis = node.at("/utc_offset").longValue();
            val name = node.at("/name").asText();

            sb.append("[").append(json.getIntervalInMilliseconds()).append("ms@")
                    .append(utcOffsetMillis).append("] ").append(name);
            emitter.send(sb.toString(), MediaType.TEXT_PLAIN);
        } catch (Exception e) {
            log.error("<sendTweet> ", e);
        } finally {
            sb.setLength(0);
        }
    }
}
