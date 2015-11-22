/*
* Created at 22:49 on 18/11/15
*/
package com.example.cli;

import com.example.component.ImmutableSetCollector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
        final String sentence = "The quick brown fox jumps over the lazy dog";
        final Map<Integer, List<String>> byLength = Stream.of(sentence.split("\\s"))
                .collect(groupingBy(String::length));
        System.out.println(byLength);
        final HashMap<String, Long> occurrences = Stream.of(sentence.split("\\s"))
                .collect(groupingBy(String::toUpperCase, HashMap::new, counting()));
        // we can chose map impl. here. helpful for building histograms with TreeMap
        System.out.println(occurrences);
    }
}
