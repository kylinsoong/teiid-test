package org.teiid.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.teiid.common.buffer.BufferManager;
import org.teiid.core.types.DataTypeManager;
import org.teiid.dqp.service.BufferService;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.runtime.EmbeddedConfiguration;
import org.teiid.services.BufferServiceImpl;

public class TestProcessBatchSize {
	
	@Test
	public void testProcessBatchSize() {
		
		BufferManager bm = getBufferService().getBufferManager();
		int processorBatchSize = bm.getProcessorBatchSize();
		
		List<ElementSymbol> elements = new ArrayList<>();
		ElementSymbol a = new ElementSymbol("a");
		a.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		ElementSymbol b = new ElementSymbol("b");
		b.setType(DataTypeManager.DefaultDataClasses.STRING);
		ElementSymbol c = new ElementSymbol("c");
		c.setType(DataTypeManager.DefaultDataClasses.STRING);
		ElementSymbol d = new ElementSymbol("d");
		d.setType(DataTypeManager.DefaultDataClasses.STRING);
		ElementSymbol e = new ElementSymbol("e");
		e.setType(DataTypeManager.DefaultDataClasses.STRING);
		
		elements.add(a);
		assertEquals(2048, bm.getProcessorBatchSize(elements));
		
		elements.add(b);
		assertEquals(1024, bm.getProcessorBatchSize(elements));
		
		elements.add(c);
		assertEquals(processorBatchSize * 2, bm.getProcessorBatchSize(elements));
		
		elements.add(d);
		elements.add(e);
		assertEquals(processorBatchSize, bm.getProcessorBatchSize(elements));
		
		
	}
	
	private BufferService getBufferService() {

		BufferServiceImpl bufferService = new BufferServiceImpl();
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		setBufferManagerProperties(bufferService, config);
		bufferService.start();
		return bufferService;
	}
	
	private void setBufferManagerProperties(BufferServiceImpl bufferService, EmbeddedConfiguration config) {
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
