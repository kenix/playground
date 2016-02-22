/*
* vwd KL
* Created by zzhao on 2/19/16 5:18 PM
*/
package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zzhao
 */
public final class CliUtils {

    private CliUtils() {
        throw new AssertionError("not for instantiation or inheritance");
    }

    public static void enterToStop(Runnable r) {
        System.out.print("'Enter to quit: '");
        final ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            es.execute(() -> {
                try {
                    try (final BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                        br.readLine();
                        r.run();
                        System.out.println("... stopped");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } finally {
            es.shutdown();
        }
    }
}
