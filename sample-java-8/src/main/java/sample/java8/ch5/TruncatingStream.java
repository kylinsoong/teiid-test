package sample.java8.ch5;

import static sample.java8.ch4.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.List;

import sample.java8.ch4.Dish;

public class TruncatingStream {

    public static void main(String[] args) {

        List<Dish> dishes = menu.stream().filter(d -> d.getCalories() > 300).limit(3).collect(toList());
    }

}
