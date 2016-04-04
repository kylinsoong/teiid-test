package org.teiid.test.lock;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
    
    private Queue<T> queue = new LinkedList<T>();
    private int capacity;
    
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }
    
    public void put(T element) throws InterruptedException {
        synchronized(this){
            while(queue.size() == capacity){
                wait();
            }
            queue.add(element);
            notify();
        }
    }

    public T take() throws InterruptedException{
        synchronized(this){
            while(queue.isEmpty()){
                wait();
            }
            T item = queue.remove();
            notify();
            return item;
        }
    }


}
