package org.teiid.common.buffer.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import org.teiid.common.buffer.FileStore;
import org.teiid.common.buffer.FileStore.FileStoreOutputStream;
import org.teiid.common.buffer.impl.FileStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;

public class FileStorageManagerTest {
	
	static Random r = new Random();
	
	static int MB = 1<<20;

	public static void main(String[] args) throws TeiidComponentException, IOException {
		

//		testInit();
//		
//		testInitialRead();
//		
//		testWrite();
//		
//		testPositionalWrite();
//		
//		testMaxSpace();
//		
//		testMaxSpaceSplit();
//		
//		testSetLength();
//		
//		testFlush();
//		
//		testGrowth();
//		
//		testWritingMultipleFiles();
//		
//		testMaxOpenFiles();
		
		testWriteBigFile();
	}

	static void testWriteBigFile() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		String tsID = "0";     //$NON-NLS-1$
        FileStore store = sm.createFileStore(tsID);
        byte[] bytes = new byte[MB * 1000];
        r.nextBytes(bytes);
        store.write(0, bytes, 0, bytes.length);
//        byte[] bytesRead = new byte[MB * 2];        
//        store.readFully(0, bytesRead, 0, bytesRead.length);
//        
//        System.out.println(Arrays.equals(bytes, bytesRead));
        System.out.println(sm.getUsedBufferSpace());
		
	}

	static void testMaxOpenFiles() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		for(int i = 0 ; i < 10000 ; i ++) {
			FileStore store = sm.createFileStore("test-" + i);
			byte[] bytes = new byte[2048];
	        r.nextBytes(bytes);
	        store.write(0, bytes, 0, bytes.length);
		}
		
		System.out.println(sm.getUsedBufferSpace());
		
	}

	static void testWritingMultipleFiles() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		FileStore store = sm.createFileStore("0");
		
		String contentOrig = new String("some file content this will stored in same tmp file with another");
        OutputStream out = store.createOutputStream();
        out.write(contentOrig.getBytes(), 0, contentOrig.getBytes().length);
        out.close();
        
        out = store.createOutputStream();
        long start = store.getLength();
        byte[] bytesOrig = new byte[2048];
        r.nextBytes(bytesOrig);
        out.write(bytesOrig, 0, 2048);
        
        byte[] readContent = new byte[2048];
        InputStream in = store.createInputStream(0, contentOrig.getBytes().length);        
    	int c = in.read(readContent, 0, 3000);
    	System.out.println(new String(readContent, 0, c));
    	in.close();
	}

	static void testGrowth() throws TeiidComponentException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		FileStore store = sm.createFileStore("0");
    	FileStoreOutputStream fsos = store.createOutputStream(1<<15);
    	
    	System.out.println(1<<5);
    	System.out.println(fsos.getBuffer().length);
	}

	static void testFlush() throws TeiidComponentException, IOException {
		
		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		FileStore store = sm.createFileStore("0");
    	FileStoreOutputStream fsos = store.createOutputStream(10);
    	
    	byte[] bytes = new byte[2048];
		r.nextBytes(bytes);
    	fsos.write(bytes);
    	fsos.write(1);
    	fsos.flush();
    	
    	byte[] bytesRead1 = new byte[2048];   
//        store.readFully(0, bytesRead1, 0, bytesRead1.length);
    	
    	InputStream in = store.createInputStream(0);
    	in.read(bytesRead1);
    	System.out.println(new String(bytesRead1));
    	System.out.println(sm.getUsedBufferSpace());
		
	}

	static void testSetLength() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		String tsID = "0";     //$NON-NLS-1$
        FileStore store = sm.createFileStore(tsID);
        
        store.setLength(1000);
        System.out.println(sm.getUsedBufferSpace());
        
        store.setLength(2000);
        System.out.println(sm.getUsedBufferSpace());
        
        store.setLength(3000);
        System.out.println(sm.getUsedBufferSpace());
	}

	static void testMaxSpaceSplit() throws TeiidComponentException {
		
		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.setMaxBufferSpace(1024);
		sm.initialize();
		
        String tsID = "0";     //$NON-NLS-1$
        
        try {
			SplittableStorageManager ssm = new SplittableStorageManager(sm);
			FileStore store = ssm.createFileStore(tsID);
			byte[] bytes = new byte[2048];
			r.nextBytes(bytes);
			store.write(0, bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println(sm.getUsedBufferSpace());
		}
	}

	static void testMaxSpace() throws TeiidComponentException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.setMaxBufferSpace(1024);
		sm.initialize();
		
		try {
			String tsID = "0";     //$NON-NLS-1$
			FileStore store = sm.createFileStore(tsID);
			byte[] bytes = new byte[2048];
			r.nextBytes(bytes);
			store.write(2048, bytes, 0, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	static void testPositionalWrite() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		String tsID = "0";     //$NON-NLS-1$
        FileStore store = sm.createFileStore(tsID);
        byte[] bytes = new byte[2048];
        r.nextBytes(bytes);
        store.write(2048, bytes, 0, bytes.length);
        byte[] bytesRead1 = new byte[2048];   
        byte[] bytesRead2 = new byte[2048]; 
        byte[] bytesRead3 = new byte[2048];
        store.readFully(0, bytesRead1, 0, bytesRead1.length);
        store.readFully(2048, bytesRead2, 0, bytesRead2.length);
        
        System.out.println(Arrays.equals(bytes, bytesRead1));
        System.out.println(Arrays.equals(bytes, bytesRead2));
        System.out.println(sm.getUsedBufferSpace());
        
        store.write(4096, bytes, 0, bytes.length);
        store.readFully(4096, bytesRead3, 0, bytesRead3.length);
        System.out.println(Arrays.equals(bytes, bytesRead3));
        System.out.println(sm.getUsedBufferSpace());
        
        store.remove();
        System.out.println(sm.getUsedBufferSpace());
        
	}

	static void testWrite() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		String tsID = "0";     //$NON-NLS-1$
        FileStore store = sm.createFileStore(tsID);
        byte[] bytes = new byte[2048];
        r.nextBytes(bytes);
        store.write(0, bytes, 0, bytes.length);
        byte[] bytesRead = new byte[2048];        
        store.readFully(0, bytesRead, 0, bytesRead.length);
        
        System.out.println(Arrays.equals(bytes, bytesRead));
        System.out.println(sm.getUsedBufferSpace());
        
	}

	static void testInitialRead() throws TeiidComponentException, IOException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		String tsID = "0";     //$NON-NLS-1$
        FileStore store = sm.createFileStore(tsID);
        
        byte[] buf = new byte[1024];
        int result = store.read(0, buf, 0, 1024);
        
        System.out.println(result);
        System.out.println(new String(buf));
        System.out.println(sm.getUsedBufferSpace());
	}

	static void testInit() throws TeiidComponentException {

		FileStorageManager sm = new FileStorageManager();
		sm.setStorageDirectory("/home/kylin/tmp/buffer");
		sm.setMaxOpenFiles(20);
		sm.initialize();
		
		System.out.println(sm.getUsedBufferSpace());
	}

}
