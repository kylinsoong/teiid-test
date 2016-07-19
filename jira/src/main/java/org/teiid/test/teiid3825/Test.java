package org.teiid.test.teiid3825;

public class Test {

    public static void main(String[] args) {

        new Thread(() -> {
            System.out.println("do something");
        }).start();
        
        Runnable r = () -> {
            System.out.println("do something");
        };
        r.run();
        
        
        
    }

}
