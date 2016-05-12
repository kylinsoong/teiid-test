package org.teiid.common.buffer.test;

import java.io.IOException;
import java.util.Random;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.impl.MemoryStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;

public class SplittableStorageManagerTest {
	
	static Random r = new Random();

	public static void main(String[] args) throws TeiidComponentException, IOException {
		
		testCreatesSpillFiles();
	}

	static void testCreatesSpillFiles() throws TeiidComponentException, IOException {

		MemoryStorageManager msm = new MemoryStorageManager();
		
		SplittableStorageManager ssm = new SplittableStorageManager(msm);
        ssm.setMaxFileSizeDirect(1024);
        ssm.initialize();
        
        FileStore store = ssm.createFileStore("0");
        byte[] bytes = new byte[4096];
        r.nextBytes(bytes);
        store.write(0, bytes, 0, bytes.length);
        
        System.out.println(msm.getCreated());
        
	}

}
