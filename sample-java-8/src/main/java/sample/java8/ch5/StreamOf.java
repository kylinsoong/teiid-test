package sample.java8.ch5;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class StreamOf {

    public static void main(String[] args) throws IOException {

        Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);
        
        int[] numbers = {2, 3, 5, 7, 11, 13};
        int sum = Arrays.stream(numbers).sum();
        System.out.println(sum);
        
        long uniqueWords = Files.lines(Paths.get("data.txt"), Charset.defaultCharset()).flatMap(line -> Arrays.stream(line.split(" "))).distinct().count();
        System.out.println("There are " + uniqueWords + " unique words in data.txt");
        
        Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }

}
