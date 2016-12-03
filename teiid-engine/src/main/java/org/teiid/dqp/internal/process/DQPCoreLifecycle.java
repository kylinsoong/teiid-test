package org.teiid.dqp.internal.process;

import static org.teiid.test.util.TestHelper.exampleRequestMessage;
import static org.teiid.test.util.TestHelper.getTransactionManager;
import static org.teiid.test.util.TestHelper.dumpMsg;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.teiid.cache.CacheConfiguration;
import org.teiid.cache.DefaultCacheFactory;
import org.teiid.client.RequestMessage;
import org.teiid.client.ResultsMessage;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.common.buffer.impl.BufferManagerImpl;
import org.teiid.core.TeiidProcessingException;
import org.teiid.dqp.service.TestBufferService;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;

public class DQPCoreLifecycle {
	
	private DQPCore core;
    private DQPConfiguration config;
    
    private SessionAwareCache<CachedResults> rs;
	private SessionAwareCache<PreparedPlan> ppc;
	
	public void lifecycle() throws CoreEnvironmentBeanException, TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
		core = new DQPCore();
		
		BufferManagerImpl bm = BufferManagerFactory.createBufferManager();
        bm.setInlineLobs(false);
        TestBufferService bs = new TestBufferService(bm, bm);
        core.setBufferManager(bs.getBufferManager());
        
        rs = new SessionAwareCache<CachedResults> ("resultset", new DefaultCacheFactory(new CacheConfiguration()), SessionAwareCache.Type.RESULTSET, 0);
        ppc = new SessionAwareCache<PreparedPlan> ("preparedplan", new DefaultCacheFactory(new CacheConfiguration()), SessionAwareCache.Type.PREPAREDPLAN, 0);
        core.setResultsetCache(rs);
        core.setPreparedPlanCache(ppc);
        
        TransactionServerImpl transactionService = new TransactionServerImpl();
        transactionService.setDetectTransactions(true);
        transactionService.setTransactionManager(getTransactionManager());
        core.setTransactionService(transactionService);
        
        config = new DQPConfiguration();
        config.setMaxActivePlans(5);
        config.setUserRequestSourceConcurrency(2);
        DefaultAuthorizationValidator daa = new DefaultAuthorizationValidator();
        daa.setPolicyDecider(new DataRolePolicyDecider());
        config.setAuthorizationValidator(daa);
        
        core.start(config);
        core.getPrepPlanCache().setModTime(1);
        core.getRsCache().setTupleBufferCache(bs.getBufferManager());
        
        executeRequest();
	}

	private void executeRequest() throws TeiidProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
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

	public static void main(String[] args) throws TeiidProcessingException, CoreEnvironmentBeanException, InterruptedException, ExecutionException, TimeoutException {
		new DQPCoreLifecycle().lifecycle();
	}

}
