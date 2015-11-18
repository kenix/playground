/*
* Created at 23:04 on 18/11/15
*/
package com.example.component;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzhao
 */
public class ObservableQueue<T> extends ObservableQueueBase<T> {

    private final Set<Subscriber<? super T>> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Observable<T> observable = Observable.create(s -> {
        subscribers.add(s); // track subscriber
        s.add(Subscriptions.create(() -> subscribers.remove(s))); // add hook upon un-subscription
    });

    public Observable<T> observe() {
        return this.observable;
    }

    @Override
    public boolean offer(T t) {
        this.subscribers.forEach(s -> s.onNext(t));
        return true;
    }

    @Override
    public void close() throws IOException {
        this.subscribers.forEach(rx.Observer::onCompleted);
    }
}
