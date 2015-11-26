/*
* Created at 01:36 on 26/11/15
*/
package com.example.component;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * @author zzhao
 */
@Slf4j
public class AsyncListenerMixin implements AsyncListener {

    public enum EventType {
        End, Timeout, Error, Start
    }

    private final BiConsumer<EventType, AsyncEvent> consumer;

    public AsyncListenerMixin(BiConsumer<EventType, AsyncEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        log.info("<onComplete> ");
        this.consumer.accept(EventType.End, event);
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        log.info("<onTimeout> ");
        this.consumer.accept(EventType.Timeout, event);
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        log.info("<onError> ");
        this.consumer.accept(EventType.Error, event);
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        log.info("<onStartAsync> ");
        this.consumer.accept(EventType.Start, event);
    }
}
