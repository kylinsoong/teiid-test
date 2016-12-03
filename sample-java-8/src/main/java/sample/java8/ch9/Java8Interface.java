package sample.java8.ch9;

public interface Java8Interface {
    
    static void A(){
        System.out.println("A");
    }
    
    default void B() {
        System.out.println("B");
    }
}
