/*
* Created at 11:12 on 21/11/15
*/
package com.example.service;

import com.example.api.RxTwitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;

/**
 * An implementation of {@link RxTwitter} based on <a href="http://twitter4j.org">twitter4j</a>.
 * <p>
 * To start this service proper twitter4j configuration is required: either through <tt>twitter4j.properties</tt>
 * in classpath, or from environment variables.
 *
 * @author zzhao
 */
@Service
@Slf4j
public class RxTwitterService implements RxTwitter {

    private final Subject<Status, Status> subject;

    private final CompletableFuture<TwitterStream> twitterStream;

    public RxTwitterService() {
        this.subject = PublishSubject.<Status>create();
        this.twitterStream = CompletableFuture
                .supplyAsync(() -> new TwitterStreamFactory().getInstance())
                .thenApply(ts -> {
                    log.info("<RxTwitterService> set up twitter subject");
                    ts.addListener(new SubscriberAdapter(this.subject));
                    ts.sample();
                    return ts;
                }).exceptionally(ex -> { // TODO: timed re-establish
                    log.error("<RxTwitterService> ", ex);
                    this.subject.onError(ex);
                    return null;
                });
    }

    @PreDestroy
    private void cleanup() {
        log.info("<cleanup> ");
        this.twitterStream.whenComplete((ts, ex) -> {
            log.info("<cleanup> clean up twitter stream");
            if (ts != null) {
                this.subject.onCompleted();
                ts.cleanUp();
            } else {
                this.subject.onError(ex);
                log.error("<cleanup> ", ex);
            }
        });
    }

    @Override
    public Observable<Status> observe() {
        return this.subject.observeOn(Schedulers.io());
    }

    private static class SubscriberAdapter extends StatusAdapter {

        private final Observer<Status> subscriber;

        public SubscriberAdapter(Observer<Status> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void onStatus(Status status) {
            this.subscriber.onNext(status);
        }

        @Override
        public void onException(Exception ex) {
            this.subscriber.onError(ex);
        }
    }
}
