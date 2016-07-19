package org.teiid.test.teiid3825;

public class FunctionMain {
    
    static void test_1(Work work){
        work.doWork();
    }
    
    static void test_2(ComplexWork work){
        work.doWork();
        work.doSomeWork();
        work.doSomeOtherWork();
    }

    public static void main(String[] args) {
        test_1(() -> {
            System.out.println("do work");
        });
        
        test_2(() -> {
            System.out.println("do work");
        });
    }

}
