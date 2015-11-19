/*
* vwd KL
* Created by zzhao on 11/18/15 12:19 PM
*/
package com.example.web;

import com.example.api.CoinMiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zzhao
 */
@RestController
@Slf4j
public class MiningController {

    private final CoinMiner coinMiner;

    private final ExecutorService es;

    @Autowired
    public MiningController(CoinMiner coinMiner) {
        this.coinMiner = coinMiner;
        this.es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    }

    @RequestMapping("/mine/{count}")
    public SseEmitter mine(@PathVariable int count) {
        final SseEmitter emitter = new SseEmitter();
        this.coinMiner.mineRx(count, this.es)
                .map(b -> BigDecimal.ONE)
                .window(500, TimeUnit.MILLISECONDS)
                .flatMap(w -> w.reduce(BigDecimal.ZERO, BigDecimal::add))
                .scan(BigDecimal::add)
                .subscribe(
                        v -> notifyProgress(emitter, v),
                        emitter::completeWithError,
                        emitter::complete
                );
        return emitter;
    }

    private void notifyProgress(SseEmitter emitter, BigDecimal val) {
        try {
            emitter.send(val);
        } catch (IOException e) {
            log.error("<notifyProgress> ", e);
        }
    }
}
