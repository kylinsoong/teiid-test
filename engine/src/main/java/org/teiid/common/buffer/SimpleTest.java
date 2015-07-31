package org.teiid.common.buffer;

public class SimpleTest {

	public static void main(String[] args) throws InterruptedException {

		System.out.println(1024 * 1024 * 8);
		
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {

				   Thread t = Thread.currentThread();
				   t.setName("Thread-1");
				   System.out.println(t.getName() + ", status = " + t.isAlive());
				   sleep(6000);
			}});
		
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {

				   Thread t = Thread.currentThread();
				   t.setName("Thread-2");
				   System.out.println(t.getName() + ", status = " + t.isAlive());
				   sleep(6000);
			}});
		

		
		t1.start();		
		t1.join();
		System.out.println(t1.getName() + ", status = " + t1.isAlive());
		
		t2.start();		
		t2.join();
		System.out.println(t2.getName() + ", status = " + t2.isAlive());
		
		
	}

	protected static void sleep(int time) {

		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
