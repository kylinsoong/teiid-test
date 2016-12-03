package org.teiid.common.buffer;

import static org.teiid.common.buffer.CacheExampleBasic.SIZE;
import static org.teiid.common.buffer.CacheExampleBasic.formCID;

import org.teiid.core.TeiidComponentException;
import org.teiid.dqp.internal.process.CachedResults;
import org.teiid.dqp.internal.process.SessionAwareCache;
import org.teiid.runtime.EmbeddedConfiguration;

public class CacheExampleCluster {

	public static void main(String[] args) throws TeiidComponentException, InterruptedException {
		
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		SessionAwareCache<CachedResults> rs = new SessionAwareCache<CachedResults>("resultset", config.getCacheFactory(), SessionAwareCache.Type.RESULTSET, config.getMaxResultSetCacheStaleness());
		
		for(int i = 0 ; i < SIZE ; i ++) {
			CachedResults cachedResults = rs.get(formCID(i));
			TupleBuffer resultsBuffer = cachedResults.getResults();
			TupleBatch tupleBatch = resultsBuffer.getBatch(1);
			System.out.println(i + ": " + tupleBatch.getTuples());
		}
	}

}
