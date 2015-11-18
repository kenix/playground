/*
* Created at 22:49 on 18/11/15
*/
package com.example.cli;

import com.example.component.ImmutableSetCollector;
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
        IntStream.range(10, 11)
                .peek(System.out::println)
                .mapToObj(Integer::toHexString)
                .map(String::toUpperCase)
                .collect(ImmutableSetCollector.create())
                .forEach(System.out::println);
    }
}
