package org.teiid.test;

import static org.teiid.query.test.TestHelper.helpSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	@Test
	public void testThreadLocal() {
		
		List<Thread> list = new ArrayList<Thread>();
		
		for(int i = 0 ; i < 10 ; i ++) {
			Thread thread = new Thread(new Runnable(){

				@Override
				public void run() {
					System.out.println(ThreadId.get());
				}});
			list.add(thread);
		}
		
		for(Thread thread : list) {
			thread.run();
		}
		
		for(Thread thread : list) {
			thread.interrupt();
		}
		
	}
	
}
