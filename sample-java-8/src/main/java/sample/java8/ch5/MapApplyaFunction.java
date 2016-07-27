package sample.java8.ch5;

import static sample.java8.ch4.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import sample.java8.ch4.Dish;

public class MapApplyaFunction {

    public static void main(String[] args) {

        menu.stream().map(Dish::getName).collect(toList()).forEach(System.out::println);
        
        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        words.stream().map(String::length).collect(toList()).forEach(System.out::println);
    }

}
