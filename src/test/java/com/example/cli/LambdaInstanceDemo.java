/*
* Created at 20:40 on 06/02/2017
*/
package com.example.cli;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zzhao
 */
public class LambdaInstanceDemo {

    public static void main(String[] args) {
        demoAnonymousClass();
        demoNonCapturingLambda();
        demoCapturingLambda();
    }

    private static void demoCapturingLambda() {
        System.out.println(FutureFactory.createWithResult(1));
        System.out.println(FutureFactory.createWithResult(2));
    }

    private static void demoNonCapturingLambda() {
        System.out.println(FutureFactory.createWithDefaultResult());
        System.out.println(FutureFactory.createWithDefaultResult());
    }

    private static void demoAnonymousClass() {
        System.out.println(FutureFactory.createWithRandomResult());
        System.out.println(FutureFactory.createWithRandomResult());
    }

    static final class FutureFactory {
        private FutureFactory() {
            throw new AssertionError("not for instantiation or inheritance");
        }

        public static Future<Integer> createWithDefaultResult() {
            return (ImmediateFuture<Integer>) () -> 0;
        }

        public static Future<Integer> createWithRandomResult() {
            return (ImmediateFuture<Integer>) () -> (int) Math.random() * 10;
        }

        public static Future<Integer> createWithResult(Integer result) {
            return (ImmediateFuture<Integer>) () -> result;
        }
    }

    @FunctionalInterface
    interface ImmediateFuture<V> extends Future<V> {
        @Override
        default boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        default boolean isCancelled() {
            return false;
        }

        @Override
        default boolean isDone() {
            return true;
        }

        @Override
        default V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return get();
        }
    }
}
