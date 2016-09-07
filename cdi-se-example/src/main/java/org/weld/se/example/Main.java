package org.weld.se.example;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {

    public static void main(String[] args) throws Exception {

        Weld weld = new Weld();
        weld.setClassLoader(new TestClassLoader(Main.class.getClassLoader()));
        WeldContainer container = weld.initialize();
        HelloWorld test = container.select(HelloWorld.class).get();
        test.sayHello();
        container.shutdown();
    }
}
