package org.teiid.dqp.internal.process;

import static org.teiid.metadata.FunctionMethod.Determinism.DETERMINISTIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.teiid.common.buffer.TeiidEmbeddedBufferManager;
import org.teiid.common.buffer.TupleBatch;
import org.teiid.common.buffer.TupleBuffer;
import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.internal.process.SessionAwareCache.CacheID;
import org.teiid.dqp.service.BufferService;
import org.teiid.query.parser.ParseInfo;
import org.teiid.runtime.EmbeddedConfiguration;

public class TeiidEmbeddedSessionAwareCache {

	public static void main(String[] args) throws TeiidComponentException {
				
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		
		BufferService bs = TeiidEmbeddedBufferManager.getBufferService();

		SessionAwareCache<CachedResults> rs = new SessionAwareCache<CachedResults>("resultset", config.getCacheFactory(), SessionAwareCache.Type.RESULTSET, config.getMaxResultSetCacheStaleness());
		rs.setTupleBufferCache(bs.getTupleBufferCache());
		List<CacheID> list = new ArrayList<>();
		
		for(int i = 0 ; i < 2000 ; i ++) {
			TupleBuffer buffer = TeiidEmbeddedBufferManager.getTupleBuffer(bs.getBufferManager());
			TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
			buffer.addTupleBatch(batch, true);
			buffer.setPrefersMemory(false);
			CachedResults cr = new CachedResults();
			cr.setResults(buffer, null);
			CacheID cid = formCID(i);
			list.add(cid);
			rs.put(cid, DETERMINISTIC, cr, 360000L);
		}
		
		for(CacheID cid : list) {
			rs.get(cid);
		}
		
		System.out.println("CacheHitCount: " + rs.getCacheHitCount() + ", CachePutCount: " + rs.getCachePutCount() + ", RequestCount: " + rs.getRequestCount() + ", TotalCacheEntries: " + rs.getTotalCacheEntries() + ", CacheTypes: " +rs.getCacheTypes());
		
	}
	
	static CacheID formCID(int i) {
		CacheID cid = new CacheID(new ParseInfo(), "id-" + i, "vdb", 1, "session", "user");
		cid.setParameters(Collections.EMPTY_LIST);
		return cid;
	}

}
