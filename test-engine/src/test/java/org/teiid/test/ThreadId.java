package org.teiid.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadId {

	private static final AtomicInteger nextId = new AtomicInteger(0);
	
	private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>(){
		public Integer get() {
			return nextId.getAndIncrement();
		}};	
	
	public static int get(){
		return threadId.get();
	}
	
	public static void main(String[] args) {
		
		List<Thread> list = new ArrayList<Thread>();
		
		for(int i = 0 ; i < 3 ; i ++) {
			Thread thread = new Thread(new Runnable(){

				@Override
				public void run() {
					System.out.println(ThreadId.get());
				}});
			list.add(thread);
		}
		
		for(Thread thread : list) {
			thread.start();
		}
		
		for(Thread thread : list) {
			thread.interrupt();
		}
	}
}
