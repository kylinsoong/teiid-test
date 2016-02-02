package org.teiid.test.embedded.plan.jta;

import static org.teiid.test.embedded.plan.jta.JTAUtils.getTransactionManager;

import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class SynchronizationExample {

    public static void main(String[] args) throws Exception {

        TransactionManager tm = getTransactionManager();
        tm.begin();
        Transaction t = tm.getTransaction();
        t.registerSynchronization(new Synchronization(){

            @Override
            public void beforeCompletion() {
                System.out.println("transaction before completion");
            }

            @Override
            public void afterCompletion(int status) {
                System.out.println("transaction after completion, status: " + status);
            }
            
        });
        tm.commit();        
        
    }

}
