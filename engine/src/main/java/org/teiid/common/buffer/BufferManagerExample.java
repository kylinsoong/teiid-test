package org.teiid.common.buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.teiid.common.buffer.BufferManager.BufferReserveMode;
import org.teiid.common.buffer.BufferManager.TupleSourceType;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.DataTypeManager;
import org.teiid.dqp.internal.process.CachedResults;
import org.teiid.dqp.internal.process.SessionAwareCache;
import org.teiid.dqp.internal.process.SessionAwareCache.CacheID;
import org.teiid.metadata.FunctionMethod.Determinism;
import org.teiid.query.parser.ParseInfo;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.test.TestHelper;
import org.teiid.runtime.EmbeddedConfiguration;

public class BufferManagerExample {

	public static void main(String[] args) throws TeiidComponentException, TeiidProcessingException {
		
		TestHelper.enableLogger();

		BufferManager bm = BufferManagerFactory.getStandaloneBufferManager();
		List<ElementSymbol> elements = new ArrayList<>();
		ElementSymbol id = new ElementSymbol("Test.PRODUCTView.product_id");
		id.setType(DataTypeManager.DefaultDataClasses.INTEGER);
		ElementSymbol symbol = new ElementSymbol("Test.PRODUCTView.symbol");
		symbol.setType(DataTypeManager.DefaultDataClasses.STRING);
		elements.add(id);
		elements.add(symbol);
		
		TupleBuffer buffer = bm.createTupleBuffer(elements, "ConnectionId", TupleSourceType.PROCESSOR);
		buffer.setForwardOnly(false);
		
		int schemaSize = bm.getSchemaSize(elements);
		bm.reserveBuffers(schemaSize, BufferReserveMode.FORCE);
		
		TupleBatch batch = new TupleBatch(0, Arrays.asList(Arrays.asList(100, "IBM"), Arrays.asList(101, "DELL"), Arrays.asList(102, "HPQ"), Arrays.asList(103, "GE"), Arrays.asList(104, "SAP"), Arrays.asList(105, "TM")));
		buffer.addTupleBatch(batch, true);
		buffer.setPrefersMemory(false);
				
		EmbeddedConfiguration config = new EmbeddedConfiguration();
		SessionAwareCache<CachedResults> rs = new SessionAwareCache<CachedResults>("resultset", config.getCacheFactory(), SessionAwareCache.Type.RESULTSET, config.getMaxResultSetCacheStaleness());
		rs.setTupleBufferCache(bm);
		CachedResults cr = new CachedResults();
		cr.setResults(buffer, null);
		CacheID cid = new CacheID(new ParseInfo(), "/*+ cache */ SELECT * FROM Test.PRODUCTView", "vdb", 1, "session", "user");
		rs.put(cid, Determinism.DETERMINISTIC, cr, 360000L);
		
		CachedResults cachedResults = rs.get(cid);
		TupleBuffer resultsBuffer = cachedResults.getResults();
		TupleBatch tupleBatch = resultsBuffer.getBatch(1);
		
		System.out.println(tupleBatch);
	}

}
