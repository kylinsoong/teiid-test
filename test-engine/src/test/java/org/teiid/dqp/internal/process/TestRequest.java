package org.teiid.dqp.internal.process;

import org.junit.Test;
import org.teiid.client.RequestMessage;
import org.teiid.common.buffer.BufferManager;
import org.teiid.common.buffer.BufferManagerFactory;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.dqp.internal.datamgr.ConnectorManager;
import org.teiid.dqp.internal.datamgr.ConnectorManagerRepository;
import org.teiid.dqp.internal.process.AuthorizationValidator.CommandType;
import org.teiid.dqp.service.AutoGenDataService;
import org.teiid.query.metadata.QueryMetadataInterface;
import org.teiid.query.parser.QueryParser;
import org.teiid.query.resolver.QueryResolver;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.tempdata.TempTableStore;
import org.teiid.query.tempdata.TempTableStore.TransactionMode;
import org.teiid.query.test.RealMetadataFactory;
import org.teiid.query.test.TestHelper;

public class TestRequest {
    
    private static final TempTableStore TEMP_TABLE_STORE = new TempTableStore("1", TransactionMode.ISOLATE_WRITES);
    
    private final static String QUERY = "SELECT * FROM pm1.g1"; 
    
    BufferManager bufferManager = BufferManagerFactory.getStandaloneBufferManager();
    
    TransactionServerImpl transactionService = new TransactionServerImpl();
    
    @Test 
    public void testValidateEntitlement() throws Exception {
        
        QueryMetadataInterface metadata = RealMetadataFactory.example1Cached();
        
        Request request = new Request();
        Command command = QueryParser.getQueryParser().parseCommand(QUERY);
        QueryResolver.resolveCommand(command, metadata);
        
        RequestMessage requestMsg = new RequestMessage();
        DQPWorkContext workContext = RealMetadataFactory.buildWorkContext(metadata, RealMetadataFactory.example1VDB());
        
        transactionService.setDetectTransactions(true);
        transactionService.setTransactionManager(TestHelper.getTransactionManager());
        request.initialize(requestMsg, bufferManager, null, transactionService, TEMP_TABLE_STORE, workContext, null);
        request.initMetadata();
        
        DefaultAuthorizationValidator drav = new DefaultAuthorizationValidator();
        DataRolePolicyDecider drpd = new DataRolePolicyDecider();
        drpd.setAllowCreateTemporaryTablesByDefault(true);
        drpd.setAllowFunctionCallsByDefault(true);
        drav.setPolicyDecider(drpd);
        request.setAuthorizationValidator(drav);
        request.validateAccess(new String[] {QUERY}, command, CommandType.USER);

        System.out.println(command);
    }
    
    @Test 
    public void testProcessRequest() throws Exception {
        
        QueryMetadataInterface metadata = RealMetadataFactory.example1Cached();
        
        RequestMessage message = new RequestMessage(QUERY);
        DQPWorkContext workContext = RealMetadataFactory.buildWorkContext(metadata, RealMetadataFactory.example1VDB());
        
        helpProcessMessage(message, null, workContext);
        
        message = new RequestMessage(QUERY);
        helpProcessMessage(message, null, workContext);
    }
    
    @Test 
    public void testCommandContext() throws Exception {
        
        QueryMetadataInterface metadata = RealMetadataFactory.example1Cached();
        
        RequestMessage message = new RequestMessage(QUERY);
        DQPWorkContext workContext = RealMetadataFactory.buildWorkContext(metadata, RealMetadataFactory.example1VDB());
        
        Request request = helpProcessMessage(message, null, workContext);
        
        System.out.println(request.context.getConnectionId());
        System.out.println(request.context.getTransactionContext());
    }
    
    private Request helpProcessMessage(RequestMessage requestMsg, SessionAwareCache<PreparedPlan> cache, DQPWorkContext workContext) throws TeiidComponentException, TeiidProcessingException {
        
        Request request = null;
        if (cache != null) {
            request = new PreparedStatementRequest(cache);
        } else {
            request = new Request();
        }
        
        workContext.getVDB().addAttchment(ConnectorManagerRepository.class, new TestConnectorManagerRepository());
        request.initialize(requestMsg, bufferManager, null, transactionService, TEMP_TABLE_STORE, workContext, null);
        DefaultAuthorizationValidator drav = new DefaultAuthorizationValidator();
        request.setAuthorizationValidator(drav);
        request.processRequest();
        return request;
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
