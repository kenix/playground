/*
* vwd KL
* Created by zzhao on 11/18/15 1:40 PM
*/
package com.example.util;

import rx.Observable;
import rx.Single;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author zzhao
 */
public class Futures {

    public static <T> Observable<T> toObservable(CompletableFuture<T> future) {
        return Observable.create(s -> future.whenComplete((result, error) -> {
            if (error != null) {
                s.onError(error);
            } else {
                s.onNext(result);
                s.onCompleted();
            }
        }));
    }

    public static <T> Single<T> toSingle(CompletableFuture<T> future) {
        return Single.create(s -> future.whenComplete((result, error) -> {
            if (error != null) {
                s.onError(error);
            } else {
                s.onSuccess(result);
            }
        }));
    }

    public static <T> CompletableFuture<List<T>> fromObservable(Observable<T> observable) {
        final CompletableFuture<List<T>> future = new CompletableFuture<>();
        observable.doOnError(future::completeExceptionally)
                .toList()
                .forEach(future::complete);
        return future;
    }

    public static <T> CompletableFuture<T> fromSingleObservable(Observable<T> observable) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        observable.doOnError(future::completeExceptionally)
                .single()
                .forEach(future::complete);
        return future;
    }
}
