/*
* Created at 00:04 on 30/11/15
*/
package com.example.util;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author zzhao
 */
public final class AsyncUtil {

    private AsyncUtil() {
        throw new AssertionError("not for instantiation or inheritance");
    }

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
            r -> {
                final Thread thread = new Thread(r, "async-util");
                thread.setDaemon(true);
                return thread;
            });

    private static TimeoutException timeout(Duration duration) {
        return new TimeoutException("timeout after: " + duration);
    }

    public static <T> CompletableFuture<T> timeoutAfter(CompletableFuture<T> original, Duration duration) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        scheduler.schedule(() -> {
            future.completeExceptionally(timeout(duration));
            original.cancel(true);
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    public static <T> CompletableFuture<T> defaultAfter(CompletableFuture<T> original, Duration duration, T value) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        scheduler.schedule(() -> {
            future.complete(value);
            original.cancel(true);
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
        return future;
    }

    public static <T> CompletableFuture<T> withTimeout(CompletableFuture<T> original, Duration duration) {
        return original.applyToEither(timeoutAfter(original, duration), Function.<T>identity());
    }

    public static <T> CompletableFuture<T> withDefaultOnTimeout(
            CompletableFuture<T> original, Duration duration, T value) {
        return original.applyToEither(defaultAfter(original, duration, value), Function.<T>identity());
    }
}
