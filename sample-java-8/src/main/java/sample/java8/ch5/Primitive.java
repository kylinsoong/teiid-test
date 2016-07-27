package sample.java8.ch5;

import static sample.java8.ch4.Dish.menu;

import java.util.stream.IntStream;

import sample.java8.ch4.Dish;

public class Primitive {

    public static void main(String[] args) {

        int calories = menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(calories);
        
        IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0).forEach(System.out::println);
//        eventNumbers.forEach(n -> System.out.println(n));
//        System.out.println();

    }

}
