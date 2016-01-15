/*
* vwd KL
* Created by zzhao on 1/15/16 2:21 PM
*/
package com.example.util.thc;

import java.util.List;
import java.util.function.Supplier;

/**
 */
public class ErasureExample {

    public interface StringList extends Supplier<List<String>> {
    }

    public void doWith(StringList stringList) {
        System.out.println("do with string list");
    }

    public interface IntList extends Supplier<List<Integer>> {
    }

    public void doWith(IntList intList) {
        System.out.println("do with int list");
    }
}
