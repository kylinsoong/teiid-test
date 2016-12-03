package org.teiid.common.buffer;

import static org.teiid.metadata.FunctionMethod.Determinism.DETERMINISTIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager.BufferReserveMode;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.dqp.internal.process.CachedResults;
import org.teiid.dqp.internal.process.SessionAwareCache;
import org.teiid.dqp.internal.process.SessionAwareCache.CacheID;
import org.teiid.query.parser.ParseInfo;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.runtime.EmbeddedConfiguration;

public class CacheExampleBasic {
	
	static List<CacheID> list = new ArrayList<>();
	static final int SIZE = 22;

	public static void main(String[] args) throws TeiidComponentException, InterruptedException {

		BufferManager bm = BufferManagerFactory.getStandaloneBufferManager();

		EmbeddedConfiguration config = new EmbeddedConfiguration();
		SessionAwareCache<CachedResults> rs = new SessionAwareCache<CachedResults>("resultset", config.getCacheFactory(), SessionAwareCache.Type.RESULTSET, config.getMaxResultSetCacheStaleness());
		rs.setTupleBufferCache(bm);
		
		
		for(int i = 0 ; i < SIZE ; i ++) {
			TupleBuffer buffer = getTupleBuffer(bm);
			TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
			buffer.addTupleBatch(batch, true);
			buffer.setPrefersMemory(false);
			CachedResults cr = new CachedResults();
			cr.setResults(buffer, null);
			CacheID cid = formCID(i);
			list.add(cid);
			rs.put(cid, DETERMINISTIC, cr, 360000L);
		}
		
		for(int i = 0 ; i < SIZE ; i ++) {
			CachedResults cachedResults = rs.get(list.get(i));
			TupleBuffer resultsBuffer = cachedResults.getResults();
			TupleBatch tupleBatch = resultsBuffer.getBatch(1);
			System.out.println(i + ": " + tupleBatch.getTuples());
		}
		
		Thread.sleep(Long.MAX_VALUE);
		
	}

	static CacheID formCID(int i) {
		return new CacheID(new ParseInfo(), "id-" + i, "vdb", 1, "session", "user");
	}

	private static TupleBuffer getTupleBuffer(BufferManager bm) throws TeiidComponentException {
		
		List<ElementSymbol> elements = new ArrayList<>();
		ElementSymbol id = new ElementSymbol("id");
		id.setType(DataTypeManager.DefaultDataClasses.OBJECT);
		elements.add(id);
		
		TupleBuffer buffer = bm.createTupleBuffer(elements, "ConnectionId", TupleSourceType.PROCESSOR);
		buffer.setForwardOnly(false);
		
		int schemaSize = bm.getSchemaSize(elements);
		bm.reserveBuffers(schemaSize, BufferReserveMode.FORCE);
		
		TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
		buffer.addTupleBatch(batch, true);
		buffer.setPrefersMemory(false);
		
		return buffer;
	}

}
