package org.teiid.test;

import static org.teiid.query.test.TestHelper.helpSerialize;

import java.io.IOException;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.teiid.dqp.internal.process.DQPWorkContext;

public class TestDQPWorkContext {
	
	public static DQPWorkContext example() {
		DQPWorkContext message = new DQPWorkContext();
		message.getSession().setVDBName("vdbName"); 
		message.getSession().setVDBVersion(1); 
		message.getSession().setApplicationName("querybuilder");
		message.getSession().setSessionId(String.valueOf(5));
		message.getSession().setUserName("userName");
		return message;
	}
	
	@Test
	public void testSerialize() throws ClassNotFoundException, IOException {
		DQPWorkContext context = helpSerialize(example());
		
		System.out.println(context.getVdbName());
		System.out.println(context.getVdbVersion());
		System.out.println(context.getAppName());
		System.out.println(context.getSessionId());
		System.out.println(context.getUserName());
	}
	
	private static final AtomicInteger nextId = new AtomicInteger(0);
    
    private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>(){

        @Override
        protected Integer initialValue() {
            return nextId.getAndIncrement();
        }};
	
	@Test
	public void testThreadLocal() {
	    
	    System.out.println(Thread.currentThread());

		for(int i = 0 ; i < 3 ; i ++){
		    System.out.println(threadId.get());
		}
		
		threadId.set(10);
		for(int i = 0 ; i < 3 ; i ++){
            System.out.println(threadId.get());
        }
		
		threadId.remove();
		for(int i = 0 ; i < 3 ; i ++){
            System.out.println(threadId.get());
        }
		
		new Thread(new Runnable(){

            @Override
            public void run() {
                
                System.out.println(Thread.currentThread());
                                
                for(int i = 0 ; i < 3 ; i ++){
                    System.out.println(threadId.get());
                }
                
                threadId.set(10);
                for(int i = 0 ; i < 3 ; i ++){
                    System.out.println(threadId.get());
                }
                
                threadId.remove();
                for(int i = 0 ; i < 3 ; i ++){
                    System.out.println(threadId.get());
                }
            }}).start();
		
		
	}
	
}
