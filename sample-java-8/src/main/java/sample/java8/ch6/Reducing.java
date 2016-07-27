package sample.java8.ch6;

import static sample.java8.ch4.Dish.menu;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.joining;

import java.util.Comparator;
import java.util.Optional;

import sample.java8.ch4.Dish;

public class Reducing {

    public static void main(String[] args) {

        long howManyDishes = menu.stream().collect(counting());
        System.out.println(howManyDishes);
        
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));
        System.out.println(mostCalorieDish.get());
        
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
        System.out.println(totalCalories);
        
        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));
        System.out.println(avgCalories);
        
//        String shortMenu = menu.stream().map(Dish::getName).collect(joining());
        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));
        System.out.println(shortMenu);
        
        
    }

}
