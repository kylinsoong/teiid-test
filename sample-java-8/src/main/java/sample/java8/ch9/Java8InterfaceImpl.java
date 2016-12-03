package sample.java8.ch9;

public class Java8InterfaceImpl implements Java8Interface {

    public void A() {
        Java8Interface.A();
    }

    public static void main(String[] args) {

        Java8InterfaceImpl impl = new Java8InterfaceImpl();
        impl.A();
        impl.B();
        
    }

}
