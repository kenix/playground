/*
* Created at 21:10 on 02/11/15
*/
package com.example.component;

import rx.Subscriber;

/**
 * @author zzhao
 */
public class PrintSubscriber extends Subscriber<Object> {

    private final String name;

    public PrintSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onCompleted() {
        System.out.printf("%s completed%n", this.name);
    }

    @Override
    public void onError(Throwable e) {
        System.out.printf("%s error: %s%n", this.name, e);
    }

    @Override
    public void onNext(Object o) {
        System.out.printf("%s next: %s%n", this.name, o);
    }
}
