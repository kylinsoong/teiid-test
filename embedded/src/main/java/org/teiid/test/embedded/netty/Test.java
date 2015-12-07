package org.teiid.test.embedded.netty;

class Dog{}

class Cat extends Dog{}

public class Test {

    public static void main(String[] args) {

        Dog d = new Dog();
        Cat c = new Cat();
        
        Dog.class.cast(c);
        
        try {
            Cat.class.cast(d);
        } catch (ClassCastException e) {
        }
        
        System.out.println();
    }

}


