package sample.java8.ch3;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerExample {
    
    public static <T> void forEach(List<T> list, Consumer<T> c){
        for(T e : list) {
            c.accept(e);
        }
    }

    public static void main(String[] args) {
        forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));
    }

}
