package org.jboss.teiid.test;

import java.util.Timer;
import java.util.TimerTask;

public class TimeTest {

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws InterruptedException {

        Timer timer = new Timer("Test Timer", true);
        TimerTask task = new TestTimerTask();
        timer.schedule(task, 2000);
        timer.schedule(task, 1000, 1000);
        
        Thread.currentThread().sleep(30000);
    }
    
    static class  TestTimerTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("Test Timer Task");
        }
        
    }

}
