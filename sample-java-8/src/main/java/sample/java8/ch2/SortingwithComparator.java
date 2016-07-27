package sample.java8.ch2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import sample.java8.ch2.FilteringApples.Apple;

public class SortingwithComparator {

    public static void main(String[] args) {

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));
        
        inventory.sort(new Comparator<Apple>(){
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }});
        
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
    }

}
