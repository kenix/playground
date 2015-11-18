/*
* vwd KL
* Created by zzhao on 11/18/15 12:21 PM
*/
package com.example.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.api.CoinMiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * @author zzhao
 */
@Service
public class CoinMinerService implements CoinMiner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SecureRandom random = new SecureRandom(Double.toHexString(
            System.currentTimeMillis()).getBytes());

    @Override
    public BigDecimal mine() {
        try {
            Thread.sleep((1 + this.random.nextInt(3)) * 1000L);
        } catch (InterruptedException e) {
            this.logger.error("<mine> mining interrupted");
            Thread.currentThread().interrupt();
        }

        final BigDecimal result = BigDecimal.valueOf(this.random.nextDouble());
        this.logger.info("<mine> {}", result.toPlainString());
        return result;
    }

    public CompletableFuture<BigDecimal> mineAsync(ExecutorService es) {
        return CompletableFuture.supplyAsync(this::mine, es);
    }

    @Override
    public Observable<BigDecimal> mineMany(int count, ExecutorService es) {
        final ReplaySubject<BigDecimal> subject = ReplaySubject.create();
        try {
            final List<CompletableFuture<BigDecimal>> futures = IntStream
                    .range(0, count)
                    .mapToObj(x -> this.mineAsync(es))
                    .collect(Collectors.toList());
            futures.forEach(f -> f.thenRun(() -> subject.onNext(BigDecimal.ONE)));
            CompletableFuture.<BigDecimal>allOf(futures.toArray(new CompletableFuture[futures.size()]))
                    .join();
            subject.onCompleted();
        } catch (Exception e) {
            this.logger.error("<mineMany> ", e);
            subject.onError(e);
        }
        return subject;
    }

    @Override
    public Observable<BigDecimal> mineRx(int count, ExecutorService es) {
        final List<Observable<BigDecimal>> futures = IntStream
                .range(0, count)
                .mapToObj(x -> this.mineAsync(es))
                .map(Futures::toObservable)
                .collect(Collectors.toList());
        return Observable.merge(futures);
    }
}
