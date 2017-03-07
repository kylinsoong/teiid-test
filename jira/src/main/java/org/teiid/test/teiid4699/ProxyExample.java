package org.teiid.test.teiid4699;

import java.util.logging.Level;

import org.teiid.client.xa.XATransactionException;
import org.teiid.dqp.service.TransactionService;
import org.teiid.logging.LogConstants;
import org.teiid.logging.LogManager;
import org.teiid.logging.MessageLevel;
import org.teiid.test.utils.EmbeddedHelper;

public class ProxyExample {
    
    static {
        EmbeddedHelper.enableLogger(Level.ALL);
    }

    public static void main(String[] args) throws XATransactionException {

        TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl();
        TransactionService transactionService = (TransactionService) LogManager.createLoggingProxy(LogConstants.CTX_TXN_LOG, transactionServiceImpl, new Class[] {TransactionService.class}, MessageLevel.DETAIL, Thread.currentThread().getContextClassLoader());
        transactionService.begin("sampleID");
        transactionService.commit("sampleID");
    }

}
