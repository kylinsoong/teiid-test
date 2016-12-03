package org.teiid.dqp.internal.process;

import static org.teiid.metadata.FunctionMethod.Determinism.DETERMINISTIC;

import java.util.Arrays;
import java.util.Collections;

import org.teiid.common.buffer.TeiidEmbeddedBufferManager;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.internal.process.SessionAwareCache.CacheID;
import org.teiid.dqp.service.BufferService;
import org.teiid.query.parser.ParseInfo;
import org.teiid.runtime.EmbeddedConfiguration;

public class TeiidEmbeddedSessionAwareCacheTupleBuffer {
	
	static CacheID cid ;

	public static void main(String[] args) throws TeiidComponentException {
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		
		BufferService bs = TeiidEmbeddedBufferManager.getBufferService();

		SessionAwareCache<CachedResults> rs = new SessionAwareCache<CachedResults>("resultset", config.getCacheFactory(), SessionAwareCache.Type.RESULTSET, config.getMaxResultSetCacheStaleness());
		rs.setTupleBufferCache(bs.getTupleBufferCache());
		
		CachedResults cacheResults = createCachedResults(bs);
		rs.put(cid, DETERMINISTIC, cacheResults, 360000L);
		
		TupleBuffer resultsBuffer = rs.get(cid).getResults();
		TupleBatch batch = resultsBuffer.getBatch(0);
		for(int i = 0 ; i < batch.getRowCount() ; i ++) {
			System.out.println(batch.getTuple(i));
		}
		
	}

	private static CachedResults createCachedResults(BufferService bs) throws TeiidComponentException {

		TupleBuffer buffer = TeiidEmbeddedBufferManager.getTupleBuffer(bs.getBufferManager());
		TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
		buffer.addTupleBatch(batch, true);
		buffer.setPrefersMemory(false);
		CachedResults cr = new CachedResults();
		cr.setResults(buffer, null);
		cid = formCID(100);
		
		return cr;
	}
	
	static CacheID formCID(int i) {
		CacheID cid = new CacheID(new ParseInfo(), "id-" + i, "vdb", 1, "session", "user");
		cid.setParameters(Collections.EMPTY_LIST);
		return cid;
	}

}
