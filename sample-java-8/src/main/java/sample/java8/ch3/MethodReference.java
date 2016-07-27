package sample.java8.ch3;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import sample.java8.ch2.FilteringApples.Apple;

public class MethodReference {

    public static void main(String[] args) {

//        example_1();
        
//        example_2();
        
        example_3();
        
    }
    
//    static String test

    static void example_3() {
        
        BiFunction<Integer, String, Apple> function = Apple::new;
        System.out.println(function.apply(100, "red"));
    }

    static void example_2() {
        List<String> str = Arrays.asList("a","b","A","B");
//        str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);
        System.out.println(str);
    }

    static void example_1() {

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));
        inventory.sort(comparing(Apple::getWeight));
        System.out.println(inventory);
    }

}
