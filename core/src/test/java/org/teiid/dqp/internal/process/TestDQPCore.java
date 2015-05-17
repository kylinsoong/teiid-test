package org.teiid.dqp.internal.process;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.teiid.adminapi.DataPolicy;
import org.teiid.adminapi.impl.DataPolicyMetadata;
import org.teiid.cache.CacheConfiguration;
import org.teiid.cache.DefaultCacheFactory;
import org.teiid.client.RequestMessage;
import org.teiid.client.ResultsMessage;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.core.TeiidProcessingException;
import org.teiid.dqp.internal.datamgr.ConnectorManager;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository;
import org.teiid.dqp.internal.process.AbstractWorkItem.ThreadState;
import org.teiid.dqp.service.AutoGenDataService;
import org.teiid.dqp.service.TestBufferService;
import org.teiid.query.metadata.CompositeMetadataStore;
import org.teiid.query.metadata.TransformationMetadata;
import org.teiid.query.test.RealMetadataFactory;
import org.teiid.query.test.TestHelper;
import org.teiid.test.util.TableRenderer;
import org.teiid.test.util.TableRenderer.Column;
import org.teiid.test.util.TableRenderer.ColumnMetaData;

public class TestDQPCore {
	
	private DQPCore core;
	private DQPConfiguration config;
	private SessionAwareCache<CachedResults> rs;
	private SessionAwareCache<PreparedPlan> ppc;
	
	private TransactionServerImpl transactionService = new TransactionServerImpl();

	@Before
	public void setUp() throws Exception {
		
		CompositeMetadataStore store = RealMetadataFactory.exampleBQTCached().getMetadataStore();
		TransformationMetadata metadata = RealMetadataFactory.createTransformationMetadata(store, "bqt");
		DQPWorkContext context = RealMetadataFactory.buildWorkContext(metadata);
		context.getVDB().getModel("BQT3").setVisible(false);
		context.getVDB().getModel("VQT").setVisible(false);
		
		HashMap<String, DataPolicy> policies = new HashMap<String, DataPolicy>();
        policies.put("foo", new DataPolicyMetadata());
        context.setPolicies(policies);
        
        context.getVDB().addAttchment(ConnectorManagerRepository.class, new TestConnectorManagerRepository());
		
        BufferManagerImpl bm = BufferManagerFactory.createBufferManager();
        bm.setInlineLobs(false);
        TestBufferService bs = new TestBufferService(bm, bm);
        
        core = new DQPCore();
        rs = new SessionAwareCache<CachedResults> ("resultset", new DefaultCacheFactory(new CacheConfiguration()), SessionAwareCache.Type.RESULTSET, 0);
        ppc = new SessionAwareCache<PreparedPlan> ("preparedplan", new DefaultCacheFactory(new CacheConfiguration()), SessionAwareCache.Type.PREPAREDPLAN, 0);
        core.setBufferManager(bs.getBufferManager());
        core.setResultsetCache(rs);
        core.setPreparedPlanCache(ppc);
        
        transactionService.setDetectTransactions(true);
        transactionService.setTransactionManager(TestHelper.getTransactionManager());
        core.setTransactionService(transactionService);
        
        config = new DQPConfiguration();
        config.setMaxActivePlans(1);
        config.setUserRequestSourceConcurrency(2);
        DefaultAuthorizationValidator daa = new DefaultAuthorizationValidator();
        daa.setPolicyDecider(new DataRolePolicyDecider());
        config.setAuthorizationValidator(daa);
        
        core.start(config);
        core.getPrepPlanCache().setModTime(1);
        core.getRsCache().setTupleBufferCache(bs.getBufferManager());
	}
	
	@After 
	public void tearDown() throws Exception {
    	DQPWorkContext.setWorkContext(new DQPWorkContext());
    	core.stop();
    }
	
	@Test
	public void testSelect() throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		String sql = "SELECT IntKey FROM BQT1.SmallA";
		String userName = "a";
		execute(sql, userName);
		
		sql = "SELECT IntKey FROM BQT1.SmallA WHERE user() = 'a'";
		execute(sql, userName);
		
		sql = "SELECT IntKey FROM BQT1.SmallA WHERE user() IN ('a') AND StringKey LIKE '1'"; 
		execute(sql, userName);
		
		sql = "SELECT IntKey FROM BQT1.SmallA WHERE user() IS NULL ";
		execute(sql, userName);
		
		sql = "SELECT user(), hasRole('foo')";
		execute(sql, userName);
		
		sql = "SELECT * FROM VQT.SmallA_2589g";
		execute(sql, userName);
		
		sql = "SELECT * FROM VQT.SmallA_2589g LIMIT 1, 1";
		execute(sql, userName);

		sql = "SELECT A.IntKey FROM BQT1.SmallA as A, BQT1.SmallA as B, (select intkey from BQT1.SmallA limit 4) as C";
		execute(sql, userName);
	}
	
	@Test
	public void testBufferLimit() throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		String sql = "SELECT A.IntKey FROM BQT1.SmallA as A, BQT1.SmallA as B, (select IntKey from BQT1.SmallA limit 4) as C";
		String userName = "a";
		String sessionid = "1";
		
		RequestMessage reqMsg = exampleRequestMessage(sql);
        reqMsg.setCursorType(ResultSet.TYPE_FORWARD_ONLY);
        
        DQPWorkContext.getWorkContext().getSession().setSessionId(sessionid);
        DQPWorkContext.getWorkContext().getSession().setUserName(userName);
        ((BufferManagerImpl)core.getBufferManager()).setProcessorBatchSize(1);
                
        Future<ResultsMessage> message = core.executeRequest(reqMsg.getExecutionId(), reqMsg);
        ResultsMessage rm = message.get(500000, TimeUnit.MILLISECONDS);
		
        dumpMsg(rm);
        
        RequestWorkItem item = core.getRequestWorkItem(DQPWorkContext.getWorkContext().getRequestID(reqMsg.getExecutionId()));
                
        int rowsPerBatch = 8;
        message = core.processCursorRequest(reqMsg.getExecutionId(), 9, rowsPerBatch);
        
        dumpMsg(rm);

        for (int i = 0; i < 10 && item.getThreadState() != ThreadState.IDLE; i++) {
        	Thread.sleep(100);
        }
        
        System.out.println(item.getThreadState());
        
        System.out.println(item.resultsBuffer.getManagedRowCount());
        
        int start = 17;
        while (true) {
            item = core.getRequestWorkItem(DQPWorkContext.getWorkContext().getRequestID(reqMsg.getExecutionId()));

	        message = core.processCursorRequest(reqMsg.getExecutionId(), start, rowsPerBatch);
	        rm = message.get(5000, TimeUnit.MILLISECONDS);
	        System.out.println(rm.getResultsList().size());
	        start += rm.getResultsList().size();
	        if (rm.getFinalRow() == rm.getLastRow()) {
	        	break;
	        }
        }
        
        reqMsg.setCursorType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        
        message = core.executeRequest(reqMsg.getExecutionId(), reqMsg);
        rm = message.get(500000, TimeUnit.MILLISECONDS);
        
        item = core.getRequestWorkItem(DQPWorkContext.getWorkContext().getRequestID(reqMsg.getExecutionId()));
        
        
	}
	
	private void dumpMsg(ResultsMessage results) {

		ColumnMetaData[] header = ColumnMetaData.Factory.form(results.getColumnNames());
	       
        TableRenderer renderer = new TableRenderer(header);
        for(List<?> list : results.getResultsList()) {
        	renderer.addRow(Column.Factory.form(list));
        }
        renderer.renderer();
	}

	@Test
	public void testHasRole() throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		String sql = "SELECT hasRole('foo')";
		String userName = "a";
		execute(sql, userName);
		
		sql = "SELECT hasRole('bar')";
		execute(sql, userName);
	}
	
	@Test
	public void testTxnAutoWrap() throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		String sql = "SELECT IntKey, StringKey, TimestampValue FROM BQT1.SmallA";
		String userName = "a";
		String sessionid = "1";
		
		RequestMessage reqMsg = exampleRequestMessage(sql);
		reqMsg.setTxnAutoWrapMode(RequestMessage.TXN_WRAP_ON);
		
		DQPWorkContext.getWorkContext().getSession().setSessionId(sessionid);
        DQPWorkContext.getWorkContext().getSession().setUserName(userName);
        
        Future<ResultsMessage> message = core.executeRequest(reqMsg.getExecutionId(), reqMsg);
        
        ResultsMessage results = message.get(500000, TimeUnit.MILLISECONDS);
        
        dumpMsg(results);
	}
	
	private void execute(String sql, String userName) throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		String sessionid = "1";
		
		RequestMessage reqMsg = exampleRequestMessage(sql);
//		reqMsg.setTxnAutoWrapMode(RequestMessage.TXN_WRAP_ON);
		
		DQPWorkContext.getWorkContext().getSession().setSessionId(sessionid);
        DQPWorkContext.getWorkContext().getSession().setUserName(userName);
        
        Future<ResultsMessage> message = core.executeRequest(reqMsg.getExecutionId(), reqMsg);
        
        ResultsMessage results = message.get(500000, TimeUnit.MILLISECONDS);
        
        ColumnMetaData[] header = ColumnMetaData.Factory.form(results.getColumnNames());
       
        TableRenderer renderer = new TableRenderer(header);
        for(List<?> list : results.getResultsList()) {
        	renderer.addRow(Column.Factory.form(list));
        }
        renderer.renderer();
	}

	
	public RequestMessage exampleRequestMessage(String sql) {
        RequestMessage msg = new RequestMessage(sql);
        msg.setCursorType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        msg.setFetchSize(10);
        msg.setPartialResults(false);
        msg.setExecutionId(100);
        return msg;
    }
	
	private static class TestConnectorManagerRepository extends ConnectorManagerRepository {

		private static final long serialVersionUID = 1151479052417494597L;

		TestConnectorManagerRepository() {
			super(true);
		}

		@Override
		public ConnectorManager getConnectorManager(String connectorName) {
			return new AutoGenDataService();
		}
		
		
	}
}
