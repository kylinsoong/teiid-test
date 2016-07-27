package sample.java8.ch5;

import static sample.java8.ch4.Dish.menu;

import java.util.Optional;

import sample.java8.ch4.Dish;

public class FindingElement {

    public static void main(String[] args) {
        
        Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();
        dish.ifPresent(d -> System.out.println(d.getName()));
    }

}
