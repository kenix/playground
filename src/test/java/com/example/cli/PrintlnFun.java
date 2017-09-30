/*
* Created at 20:52 on 05/02/2017
*/
package com.example.cli;

import java.util.stream.IntStream;

/**
 * @author the java specialist
 */
public class PrintlnFun {

    public static void main(String[] args) {
        serialPrintln();
        //synchronized (System.out) {
        parallelPrintln();
        //}
    }

    private static void serialPrintln() {
        System.out.println("hello world");
        IntStream.range(0, 4).forEach(System.out::println);
    }

    private static void parallelPrintln() {
        System.out.println("hello world");
        IntStream.range(0, 4).parallel().forEach(System.out::println);
    }
}
