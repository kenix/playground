/*
* Created at 22:49 on 18/11/15
*/
package com.example.cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

/**
 * @author zzhao
 */
@Component
public class PlayFieldCLR implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        final int sum = IntStream.range(0, 10)
                .peek(System.out::println)
                .sum();
        System.out.println(sum);
    }
}
