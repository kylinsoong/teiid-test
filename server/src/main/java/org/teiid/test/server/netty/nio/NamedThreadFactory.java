package org.teiid.test.server.netty.nio;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	
	private AtomicInteger threadNumber = new AtomicInteger();
	private String baseName;
	
	public NamedThreadFactory(String name) {
		this.baseName = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		String threadName = baseName + "-" + threadNumber.getAndIncrement();
		Thread t = new Thread(r, threadName);
		return t;
	}

}
