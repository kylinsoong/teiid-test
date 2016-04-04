package org.teiid.test.lock;

public class SimpleRun_1 {

    public static void main(String[] args) throws InterruptedException {

        final BlockingQueue<String> queue = new BlockingQueue<>(3);
        
        new Thread(new Runnable(){
            public void run() {
                Thread.currentThread().setName("Thread 1");
                try {
                    for(int i = 0 ; i < 3 ; i ++){
                        queue.put("");
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }}).start();
        
        Thread.sleep(Long.MAX_VALUE);
        
    }

}
