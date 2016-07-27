package sample.java8.ch5;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Summing {

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
//        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
//        int sum = numbers.stream().reduce(0, Integer::sum);
//        Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
        Optional<Integer> sum = numbers.stream().reduce(Integer::sum);
        System.out.println(sum.get());
        
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println(product);
        
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        
        System.out.println(max.get());
        System.out.println(min.get());
    }

}
