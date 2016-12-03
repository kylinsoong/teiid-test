package org.teiid.common.buffer.test;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.teiid.common.buffer.BatchManager;
import org.teiid.common.buffer.LobManager;
import org.teiid.common.buffer.Serializer;
import org.teiid.common.buffer.impl.SizeUtility;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.DataTypeManager;


public class TestBatchManager implements BatchManager, Serializer<List<? extends List<?>>> {
	
	final Long id;
	SizeUtility sizeUtility;
	private WeakReference<TestBatchManager> ref = new WeakReference<TestBatchManager>(this);
	private PhantomReference<Object> cleanup;
	AtomicBoolean prefersMemory = new AtomicBoolean();
	String[] types;
	private LobManager lobManager;
	private long totalSize;
	private long rowsSampled;
	
	public TestBatchManager(Long newID, Class<?>[] types){
		this.id = newID;
		this.sizeUtility = new SizeUtility(types);
		this.types = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			this.types[i] = DataTypeManager.getDataTypeName(types[i]);
		}
	}

	@Override
	public List<List<?>> getBatch(Long batch, boolean retain)throws TeiidComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Long batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrefersMemory(boolean prefers) {
		this.prefersMemory.set(prefers);
	}

	@Override
	public boolean prefersMemory() {
		return this.prefersMemory.get();
	}

	@Override
	public Long createManagedBatch(List<? extends List<?>> batch, Long previous, boolean removeOld) throws TeiidComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Reference<? extends BatchManager> getBatchManagerReference() {
		return this.ref;
	}

	@Override
	public String[] getTypes() {
		return types;
	}

	@Override
	public int getRowSizeEstimate() {
		return 0;
	}

	@Override
	public void serialize(List<? extends List<?>> obj, ObjectOutput oos) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<? extends List<?>> deserialize(ObjectInput ois) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean useSoftCache() {
		return this.prefersMemory.get();
	}

	@Override
	public Long getId() {
		return id;
	}

}
