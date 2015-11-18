/*
* vwd KL
* Created by zzhao on 11/18/15 12:17 PM
*/
package com.example.api;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import rx.Observable;

/**
 * @author zzhao
 */
public interface CoinMiner {

    BigDecimal mine();

    CompletableFuture<BigDecimal> mineAsync(ExecutorService es);

    Observable<BigDecimal> mineMany(int count, ExecutorService es);

    Observable<BigDecimal> mineRx(int count, ExecutorService es);
}
