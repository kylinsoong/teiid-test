package org.teiid.test.teiid3825;

@FunctionalInterface
public interface ComplexWork extends Work {

    default public void doSomeWork(){
        System.out.println("Doing some work in interface impl...");
    }
    
    default public void doSomeOtherWork(){
        System.out.println("Doing some other work in interface impl...");
    }
    
}
