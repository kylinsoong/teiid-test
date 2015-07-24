package org.teiid.common.buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.teiid.common.buffer.BufferManager.BufferReserveMode;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.dqp.service.BufferService;
import org.teiid.example.EmbeddedHelper;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.services.BufferServiceImpl;

/**
 * 
 * This example used to simulate the procedure of Data Management in Teiid Embedded
 * 
 *  https://docs.jboss.org/author/display/TEIID/Data+Management
 * 
 * @author kylin
 *
 */
public class TeiidEmbeddedBufferManager {

	public static void main(String[] args) throws TeiidComponentException {
		
		EmbeddedHelper.enableLogger(Level.ALL);

		/**
		 * 1. BufferManagerImpl is initiated in EmbeddedServer's start() method
		 */
		BufferService bs = getBufferService();
		
		/**
		 * 2. TupleBuffer be created in BatchCollector's constructor method
		 */
		TupleBuffer buffer = getTupleBuffer(bs.getBufferManager());
		
		/**
		 * 3. Reserving buffer in QueryProcessor's init() method 
		 */
		int reserved = bmReserving(bs.getBufferManager());
		
		/**
		 * 4. Releasing reserved buffer space
		 */
		bs.getBufferManager().releaseBuffers(reserved);
		
		/**
		 * 5. distribute TupleBuffer
		 */
		
		distributeTupleBuffer(buffer, bs.getBufferManager());
		
		
		System.out.println("HeapCacheMemoryInUseKB: " + ((BufferServiceImpl) bs).getHeapCacheMemoryInUseKB());
		System.out.println("HeapMemoryInUseByActivePlansKB: " + ((BufferServiceImpl) bs).getHeapMemoryInUseByActivePlansKB());
		System.out.println("DiskWriteCount: " + ((BufferServiceImpl) bs).getDiskWriteCount());
		System.out.println("DiskReadCount: " + ((BufferServiceImpl) bs).getDiskReadCount());
		System.out.println("CacheReadCount: " + ((BufferServiceImpl) bs).getCacheReadCount());
		System.out.println("CacheWriteCount: " + ((BufferServiceImpl) bs).getCacheWriteCount());
		System.out.println("DiskBufferSpaceMB: " + ((BufferServiceImpl) bs).getUsedDiskBufferSpaceMB());
		
	}

	private static void distributeTupleBuffer(TupleBuffer buffer, BufferManager bufferManager) throws TeiidComponentException {
		
		List<List<?>> listOfTupleLists = new ArrayList<>();
		listOfTupleLists.add(Arrays.asList(100, "IBM"));
		listOfTupleLists.add(Arrays.asList(101, "DELL"));
		listOfTupleLists.add(Arrays.asList(102, "HPQ"));
		listOfTupleLists.add(Arrays.asList(103, "GE"));
		listOfTupleLists.add(Arrays.asList(104, "SAP"));
		listOfTupleLists.add(Arrays.asList(105, "TM"));

		TupleBatch batch = new TupleBatch(0, listOfTupleLists);
		buffer.addTupleBatch(batch, true);
		
		bufferManager.distributeTupleBuffer(buffer.getId(), buffer);
	}

	private static int bmReserving(BufferManager bufferManager) {

		int count = bufferManager.getSchemaSize(getOutputElements());
		BufferReserveMode mode = BufferReserveMode.FORCE;
		return bufferManager.reserveBuffers(count, mode);
	}

	public static TupleBuffer getTupleBuffer(BufferManager bm) throws TeiidComponentException {

		TupleBuffer buffer = bm.createTupleBuffer(getOutputElements(), "ConnectionId", TupleSourceType.PROCESSOR);
		buffer.setForwardOnly(false);
		
		return buffer;
	}

	private static List<ElementSymbol> getOutputElements() {

		List<ElementSymbol> elements = new ArrayList<>();
		ElementSymbol id = new ElementSymbol("Test.PRODUCTView.product_id");
		id.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		ElementSymbol symbol = new ElementSymbol("Test.PRODUCTView.symbol");
		symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
		elements.add(id);
		elements.add(symbol);
		return elements;
	}

	public static BufferService getBufferService() {

		BufferServiceImpl bufferService = new BufferServiceImpl();
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		config.setBufferDirectory("/home/kylin/tmp/buffer");
		config.setProcessorBatchSize(256);
		setBufferManagerProperties(bufferService, config);
		
		bufferService.start();
		
		return bufferService;
	}

	private static void setBufferManagerProperties(BufferServiceImpl bufferService, EmbeddedConfiguration config) {
		bufferService.setUseDisk(config.isUseDisk());
		if (config.isUseDisk()) {
			if (config.getBufferDirectory() == null) {
				config.setBufferDirectory(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
			}
			bufferService.setDiskDirectory(config.getBufferDirectory());
		}
		
		if(config.getProcessorBatchSize() != -1)
			bufferService.setProcessorBatchSize(config.getProcessorBatchSize());
		if(config.getMaxReserveKb() != -1)
			bufferService.setMaxReserveKb(config.getMaxReserveKb());
		if(config.getMaxProcessingKb() != -1)
			bufferService.setMaxProcessingKb(config.getMaxProcessingKb());
		bufferService.setInlineLobs(config.isInlineLobs());
		if(config.getMaxOpenFiles() != -1)
			bufferService.setMaxOpenFiles(config.getMaxOpenFiles());
		
		if(config.getMaxBufferSpace() != -1)
			bufferService.setMaxBufferSpace(config.getMaxBufferSpace());
		if(config.getMaxFileSize() != -1) 
			bufferService.setMaxFileSize(config.getMaxFileSize());
		bufferService.setEncryptFiles(config.isEncryptFiles());
		if(config.getMaxStorageObjectSize() != -1) {
			bufferService.setMaxStorageObjectSize(config.getMaxStorageObjectSize());
		}
		bufferService.setMemoryBufferOffHeap(config.isMemoryBufferOffHeap());
		if(config.getMemoryBufferSpace() != -1)
			bufferService.setMemoryBufferSpace(config.getMemoryBufferSpace());
		
	}

}
