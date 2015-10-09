package org.teiid.test.transaction;

import javax.transaction.TransactionManager;

import org.teiid.dqp.internal.process.TransactionServerImpl;
import org.teiid.example.EmbeddedHelper;

public class TransactionServiceTest {

    public static void main(String[] args) throws Exception {

        TransactionServerImpl transactionService = new TransactionServerImpl();
        
        TransactionManager tm = EmbeddedHelper.getTransactionManager();
        
        transactionService.setDetectTransactions(true);
        transactionService.setTransactionManager(tm);
        
        transactionService.begin("gfrZVTTTZajv");
        
    }

}
