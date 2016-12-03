package sample.java8.ch9;

public class C2 implements A, B {

    public void sayHello() {
//        A.super.hello();
        B.super.hello();
    }

    public static void main(String[] args) {

        new C2().hello();
    }

}
