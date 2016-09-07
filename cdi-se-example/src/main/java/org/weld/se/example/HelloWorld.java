package org.weld.se.example;

import javax.inject.Singleton;

@Singleton
public class HelloWorld {
    
    public void sayHello(){
        System.out.println("Hello World");
    }

}
