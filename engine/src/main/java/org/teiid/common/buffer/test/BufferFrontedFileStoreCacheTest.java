package org.teiid.common.buffer.test;


import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.WeakReference;

import org.teiid.common.buffer.CacheEntry;
import org.teiid.common.buffer.Serializer;
import org.teiid.common.buffer.StorageManager;
import org.teiid.common.buffer.impl.BufferFrontedFileStoreCache;
import org.teiid.common.buffer.impl.FileStorageManager;
import org.teiid.common.buffer.impl.SplittableStorageManager;
import org.teiid.core.TeiidComponentException;

public class BufferFrontedFileStoreCacheTest {
	
	private final static class SimpleSerializer implements Serializer<Integer> {
		@Override
		public Integer deserialize(ObjectInput ois)
				throws IOException, ClassNotFoundException {
			Integer result = ois.readInt();
			for (int i = 0; i < result; i++) {
				System.out.println( ois.readInt());
			}
			return result;
		}

		@Override
		public Long getId() {
			return 1l;
		}

		@Override
		public void serialize(Integer obj, ObjectOutput oos)
				throws IOException {
			oos.writeInt(obj);
			for (int i = 0; i < obj; i++) {
				oos.writeInt(i);
			}
		}

		@Override
		public boolean useSoftCache() {
			return false;
		}
	}

	public static void main(String[] args) throws TeiidComponentException {

		testAddGetMultiBlock();
		
	}

	static void testAddGetMultiBlock() throws TeiidComponentException {

		BufferFrontedFileStoreCache cache = createBufferFrontedFileStoreCache();
		CacheEntry ce = new CacheEntry(2l);
		Serializer<Integer> s = new SimpleSerializer();
		cache.createCacheGroup(s.getId());
		Integer cacheObject = Integer.valueOf(123);
		ce.setObject(cacheObject);
		cache.addToCacheGroup(s.getId(), ce.getId());
		cache.add(ce, s);
		CacheEntry cer = cache.get(cache.lockForLoad(2l, s), 2l, new WeakReference<Serializer<?>>(s));
		cache.unlockForLoad(cache.lockForLoad(2l, s));
		
		System.out.println(cer.getObject());
	}

	static BufferFrontedFileStoreCache createBufferFrontedFileStoreCache() throws TeiidComponentException {
		
		FileStorageManager fsm = new FileStorageManager();
		fsm.setStorageDirectory("/home/kylin/tmp/buffer");
		fsm.setMaxOpenFiles(64);
		fsm.setMaxBufferSpace(1<<32);
		
		SplittableStorageManager ssm = new SplittableStorageManager(fsm);
        ssm.setMaxFileSize(2048);
        StorageManager sm = ssm;

		BufferFrontedFileStoreCache fsc  = new BufferFrontedFileStoreCache();
//		fsc.setBufferManager(this.bufferMgr);
        fsc.setMaxStorageObjectSize(1 << 26);
        fsc.setDirect(false);
        fsc.setMemoryBufferSpace(1<<26);
        
        fsc.setStorageManager(sm);
        fsc.initialize();
		return fsc;
		
	}

}
