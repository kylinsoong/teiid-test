package sample.java8.ch5;

import static sample.java8.ch4.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.List;

import sample.java8.ch4.Dish;

public class FilteringAndSlicing {

    public static void main(String[] args) {
//        List<Dish> vegetarianMenu = menu.stream().filter((Dish d) -> d.isVegetarian()).collect(toList());
//        List<Dish> vegetarianMenu = menu.stream().filter(d -> d.isVegetarian()).collect(toList());
        List<Dish> vegetarianMenu = menu.stream().filter(Dish::isVegetarian).collect(toList());
                
    }

}
