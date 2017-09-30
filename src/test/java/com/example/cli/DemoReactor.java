/*
* Created at 21:01 on 11/03/2017
*/
package com.example.cli;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;

/**
 * @author zzhao
 */
@Slf4j
public class DemoReactor {

    public static void main(String[] args) {
//        logBlocking();
//        subscribeBlocking();
//        logBlockingBound2();
//        logBlockingBound2OnParaSub();
//        logBlockingBound2WithParaSub();
//        logBlockingBound2WithParaSubPub();
//        logBlockingBound2WithParaPub();
//        scatterAndGatherBad();
//        scatterAndGatherBad2();
        scatterAndGatherGood();
    }

    private static void scatterAndGatherGood() {
        separeteLog();
        Flux.range(1, 10)
                .log()
                .flatMap(DemoReactor::fetch, 4)
                .collect(ArrayList<Integer>::new, ArrayList::add)
                .subscribe(l -> log.info("<scatterAndGatherGood> {}", l.stream().mapToInt(Integer::intValue).sum()));
        delay(5000);
    }

    private static void scatterAndGatherBad2() {
        separeteLog();
        Flux.range(1, 10)
                .log()
                .map(DemoReactor::fetch)
                .subscribe(m -> System.out.println(m.block()));
    }

    private static Mono<Integer> fetch(int val) {
        return Mono.fromCallable(() -> block(val)).subscribeOn(Schedulers.parallel());
    }

    private static void scatterAndGatherBad() {
        separeteLog();
        Flux.range(1, 10)
                .log()
                .map(DemoReactor::block)
                .collect(ArrayList<Integer>::new, ArrayList::add)
                .subscribe(l -> System.out.println(l.stream().mapToInt(Integer::intValue).sum()));
    }

    private static int block(int val) {
        delay(1000);
        return val;
    }

    private static void logBlockingBound2WithParaPub() {
        separeteLog();
        final Scheduler pub = Schedulers.newParallel("pub");
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .publishOn(pub, 2)
                .subscribe(v -> log.info("<logBlockingBound2WithParaSub> {}", v));
        delay(100);
        pub.dispose();
    }

    private static void logBlockingBound2WithParaSubPub() {
        separeteLog();
        final Scheduler sub = Schedulers.newParallel("sub");
        final Scheduler pub = Schedulers.newParallel("pub");
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .subscribeOn(sub)
                .publishOn(pub, 2)
                .subscribe(v -> log.info("<logBlockingBound2WithParaSub> {}", v));
        delay(100);
        sub.dispose();
        pub.dispose();
    }

    private static void logBlockingBound2WithParaSub() {
        separeteLog();
        Flux.just("red", "white", "blue")
                .log()
                .flatMap(v -> Mono.just(v.toUpperCase()).subscribeOn(Schedulers.parallel()), 2)
                .subscribe(v -> log.info("<logBlockingBound2WithParaSub> {}", v));
        delay(100);
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("<delay> ", e);
            Thread.interrupted();
        }
    }

    private static void logBlockingBound2OnParaSub() {
        separeteLog();
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .subscribeOn(Schedulers.parallel())
                .limitRate(2)
                .subscribe();
        delay(100);
    }

    private static void logBlockingBound2() {
        separeteLog();
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .limitRate(2)
                .subscribe();
    }

    private static void subscribeBlocking() {
        separeteLog();
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .subscribe(System.out::println);
    }

    private static void separeteLog() {
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private static void logBlocking() {
        separeteLog();
        Flux.just("red", "white", "blue")
                .log()
                .map(String::toUpperCase)
                .subscribe();
    }
}
