package sample.java8.ch9;

public interface A {

    default void hello() {
        System.out.println("Hello from A");
    }
}
