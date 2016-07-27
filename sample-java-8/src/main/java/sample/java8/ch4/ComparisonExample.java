package sample.java8.ch4;

import static sample.java8.ch4.Dish.menu;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparisonExample {

    public static void main(String[] args) {

        java_7();
        
        java_8();
    }

    static void java_8() {

        List<String> lowCaloricDishesName = menu.stream().filter(d -> d.getCalories() < 400).sorted(comparing(Dish::getCalories)).map(Dish::getName).collect(toList());
        lowCaloricDishesName.forEach(System.out::println);        
    }

    static void java_7() {
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for(Dish d : menu){
            if(d.getCalories() < 400){
                lowCaloricDishes.add(d);
            }
        }
        Collections.sort(lowCaloricDishes, new Comparator<Dish>(){
            public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }});
        List<String> lowCaloricDishesName = new ArrayList<>();
        for(Dish d : lowCaloricDishes){
            lowCaloricDishesName.add(d.getName());
        }
        System.out.println(lowCaloricDishesName);
    }

}
