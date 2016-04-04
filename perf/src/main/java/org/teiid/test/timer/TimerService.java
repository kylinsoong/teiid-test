package org.teiid.test.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.teiid.core.util.NamedThreadFactory;

public class TimerService {

    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10, new NamedThreadFactory("Asynch Worker"));
        scheduler.scheduleAtFixedRate(new Runnable(){

            @Override
            public void run() {

                System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
            }}, 10000, 1000, TimeUnit.MILLISECONDS);
        
        Thread.sleep(Long.MAX_VALUE);
    }

}
