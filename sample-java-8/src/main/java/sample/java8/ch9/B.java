package sample.java8.ch9;

public interface B extends A{

    default void hello() {
        System.out.println("Hello from B");
    }
}
