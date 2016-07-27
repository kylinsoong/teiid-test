package sample.java8.ch5;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

public class FlatteningStream {

    public static void main(String[] args) {

        List<String> words = Arrays.asList("Hello", "World");
        words.stream().map(word -> word.split("")).flatMap(Arrays::stream).distinct().collect(toList()).forEach(System.out::println);
    }

}
