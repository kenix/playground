/*
* Created at 11:12 on 21/11/15
*/
package com.example.service;

import com.example.api.RxMeetup;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.pipeline.ssl.DefaultFactories;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientPipelineConfigurator;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.observables.StringObservable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static com.example.util.AsyncUtil.withTimeout;

/**
 * @author zzhao
 */
@Service
@Slf4j
public class RxMeetupService implements RxMeetup {

    private final Subject<String, String> subject;

    private final CompletableFuture<HttpClient<ByteBuf, ByteBuf>> meetupClient;

    public RxMeetupService() {
        this.subject = PublishSubject.<String>create();
        this.meetupClient = withTimeout(CompletableFuture
                .supplyAsync(() -> RxNetty.<ByteBuf, ByteBuf>newHttpClientBuilder("stream.meetup.com", 443)
                        .pipelineConfigurator(new HttpClientPipelineConfigurator<>())
                        .withSslEngineFactory(DefaultFactories.trustAll())
                        .build()), Duration.ofSeconds(2))
                .thenApply(client -> {
                    log.info("<RxMeetupService> set up meetup subject");
                    final Observable<String> jsons = StringObservable.split(client
                            .submit(HttpClientRequest.createGet("/2/open_events"))
                            .flatMap(AbstractHttpContentHolder::getContent)
                            .map(bb -> bb.toString(StandardCharsets.UTF_8)), "\n");
                    jsons.subscribe(this.subject);
                    return client;
                }).exceptionally(ex -> { // TODO: timed re-connect
                    log.error("<RxMeetupService> ", ex);
                    this.subject.onError(ex);
                    return null;
                });
    }

    @PreDestroy
    private void cleanup() {
        log.info("<cleanup> ");
        this.meetupClient.whenComplete((client, ex) -> {
            log.info("<cleanup> clean up meetup stream");
            if (client != null) {
                this.subject.onCompleted();
                client.shutdown();
            } else {
                this.subject.onError(ex);
                log.error("<cleanup> ", ex);
            }
        });
    }

    @Override
    public Observable<String> observe() {
        return this.subject.observeOn(Schedulers.io());
    }
}
