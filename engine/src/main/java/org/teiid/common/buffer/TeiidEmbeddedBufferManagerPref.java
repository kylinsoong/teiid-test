package org.teiid.common.buffer;

import static org.teiid.common.buffer.TeiidEmbeddedBufferManager.getBufferService;
import static org.teiid.common.buffer.TeiidEmbeddedBufferManager.getTupleBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.service.BufferService;
import org.teiid.example.EmbeddedHelper;
import org.teiid.services.BufferServiceImpl;

public class TeiidEmbeddedBufferManagerPref {
	
	static final int SIZE = 3000;

	public static void main(String[] args) throws TeiidComponentException, InterruptedException {

		EmbeddedHelper.enableLogger(Level.ALL);
		
		BufferService bs = getBufferService();
		
		TupleBuffer buffer = getTupleBuffer(bs.getBufferManager());
		
		for(int i = 0 ; i < SIZE ; i ++) {
			TupleBatch batch = createBatch(buffer.getBatchSize());
			buffer.addTupleBatch(batch, true);
		}
		
		Thread.sleep(1000);
		
		BufferManagerImpl bm = (BufferManagerImpl) bs.getBufferManager();
		System.out.println(bm.getActiveBatchBytes());
		System.out.println(bm.getBatchesAdded());
		System.out.println(bm.getMemoryCacheEntries());
		
		Thread.sleep(1000);
		
		int begin = 1;
		for(int i = 0 ; i < SIZE ; i ++) {
			TupleBatch batch = buffer.getBatch(begin);
			begin += buffer.getBatchSize();
		}
		
		Thread.sleep(1000);
		
		System.out.println("HeapCacheMemoryInUseKB: " + ((BufferServiceImpl) bs).getHeapCacheMemoryInUseKB());
		System.out.println("HeapMemoryInUseByActivePlansKB: " + ((BufferServiceImpl) bs).getHeapMemoryInUseByActivePlansKB());
		System.out.println("DiskWriteCount: " + ((BufferServiceImpl) bs).getDiskWriteCount());
		System.out.println("DiskReadCount: " + ((BufferServiceImpl) bs).getDiskReadCount());
		System.out.println("CacheReadCount: " + ((BufferServiceImpl) bs).getCacheReadCount());
		System.out.println("CacheWriteCount: " + ((BufferServiceImpl) bs).getCacheWriteCount());
		System.out.println("DiskBufferSpaceMB: " + ((BufferServiceImpl) bs).getUsedDiskBufferSpaceMB());
		
	}

	final static Random r = new Random();
	
	static int rowset = 1;
	
	private static TupleBatch createBatch(int batchSize) {
		List<List<?>> listOfTupleLists = new ArrayList<>();
		listOfTupleLists.add(Arrays.asList(100, formString(1024 * 200)));
		listOfTupleLists.add(Arrays.asList(101, formString(1024 * 200)));
		listOfTupleLists.add(Arrays.asList(102, formString(1024 * 200)));
		listOfTupleLists.add(Arrays.asList(103, formString(1024 * 200)));


		TupleBatch batch = new TupleBatch(rowset, listOfTupleLists);
		rowset += batchSize;
		return batch;
	}
	
	static byte[] buf = new byte[1024 * 1024 * 1];
	
	private static String formString(int size) {
		
		r.nextBytes(buf);
		return new String(buf);
	}

}
