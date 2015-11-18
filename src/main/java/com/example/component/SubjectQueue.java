/*
* Created at 23:04 on 18/11/15
*/
package com.example.component;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import java.io.IOException;

/**
 * @author zzhao
 */
public class SubjectQueue<T> extends ObservableQueueBase<T> {

    private final Subject<T, T> subject = PublishSubject.<T>create().toSerialized();

    public Observable<T> observe() {
        return this.subject;
    }

    @Override
    public boolean offer(T t) {
        this.subject.onNext(t);
        return true;
    }

    @Override
    public void close() throws IOException {
        this.subject.onCompleted();
    }
}
