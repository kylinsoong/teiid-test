package org.teiid.dqp.internal.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.TupleBatch;

public class MemoryTest {
	
	List<Object> list = new ArrayList<>();
	
	public void test1(){
		for(int i = 0 ; i < 20 ; i ++) {
			list.add(new byte[1024 * 1024 * 100]);
		}
	}
	
	public void test2(){
		for(int i = 0 ; i < 20 ; i ++) {
			list.add(Arrays.asList(100, new byte[1024 * 1024 * 100]));
		}
	}
	
	public void test3(){
		for(int i = 0 ; i < 20 ; i ++) {
			list.add(Arrays.asList(Arrays.asList(100, new byte[1024 * 1024 * 100])));
		}
	}
	
	public void test4(){
		for(int i = 0 ; i < 20 ; i ++) {
			list.add(new TupleBatch(0, Arrays.asList(Arrays.asList(100, new byte[1024 * 1024 * 100]))));
		}
	}

	public static void main(String[] args) {
		
		MemoryTest test = new MemoryTest();
		
		test.test4();
		

//		TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, new byte[1024 * 1024 * 100])));
	}

}
