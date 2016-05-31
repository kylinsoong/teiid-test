package org.teiid.test.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.teiid.core.util.NamedThreadFactory;

public class TimerServiceScheduleTest {

    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10, new NamedThreadFactory("Asynch Worker"));

        scheduler.schedule(new Runnable(){

            @Override
            public void run() {

                System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis());
            }}, 5000, TimeUnit.MILLISECONDS);
        
        Thread.sleep(Long.MAX_VALUE);
    }

}
