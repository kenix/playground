/*
* Created at 13:27 on 21/11/15
*/
package com.example.web;

import com.example.api.RxTwitter;
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
import twitter4j.Status;

import java.util.concurrent.TimeUnit;

/**
 * @author zzhao
 */
@RestController
@RequestMapping("/twitter")
@Slf4j
public class RxTwitterController {

    private final RxTwitter twitter;

    @Autowired
    public RxTwitterController(RxTwitter twitter) {
        this.twitter = twitter;
    }

    /**
     * Stream public twitter tweets for given milliseconds.
     * <p>
     * If no tweets can be streamed, the returned stream will timeout after given millisecond plus half second.
     * TODO: enable filter on language, hashtag, user etc..
     *
     * @param millis milliseconds to stream, supports upto {@value Short#MAX_VALUE}
     * @return Stream of tweets in following format:
     * <pre>[{elapsed milliseconds from last received tweet}@{user.name}#{language}] {tweet text}</pre>
     */
    @RequestMapping(value = "/{millis}", method = RequestMethod.GET)
    public SseEmitter streamTimed(@PathVariable short millis) {
        val emitter = new SseEmitter(millis + 500L);
        val sb = new StringBuilder();

        this.twitter.observe()
                .take(millis, TimeUnit.MILLISECONDS)
                .timeInterval()
                .subscribe(
                        st -> this.sendTweet(st, emitter, sb),
                        emitter::completeWithError,
                        emitter::complete
                );
        return emitter;
    }

    private void sendTweet(TimeInterval<Status> tweet, SseEmitter emitter, StringBuilder sb) {
        sb.append("[").append(tweet.getIntervalInMilliseconds()).append("ms@")
                .append(tweet.getValue().getUser().getName())
                .append("#").append(tweet.getValue().getLang())
                .append("] ").append(tweet.getValue().getText());
        try {
            emitter.send(sb.toString(), MediaType.TEXT_PLAIN);
        } catch (Exception e) {
            log.error("<sendTweet> ", e);
        } finally {
            sb.setLength(0);
        }
    }
}
