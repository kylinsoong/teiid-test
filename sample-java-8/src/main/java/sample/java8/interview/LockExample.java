package sample.java8.interview;

public class LockExample {
    
    public void foo() {
        synchronized(this){   
            zoo();
        }
    }
    
    public void zoo() {
        synchronized(this) {
            System.out.println("zoo");
        }
    }
}
