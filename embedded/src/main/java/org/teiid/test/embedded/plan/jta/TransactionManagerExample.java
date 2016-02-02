package org.teiid.test.embedded.plan.jta;

import static org.teiid.test.embedded.plan.jta.JTAUtils.getTransactionManager;

import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class TransactionManagerExample {

    public static void main(String[] args) throws Exception {
        
//        example_1();
        
//        example_2();
        
//        example_3();
        
//        example_4();
        
//        example_5();
        
        example_6();

    }

    static void example_6() throws Exception {
        TransactionManager tm = getTransactionManager();
        tm.begin();
        Transaction t = tm.getTransaction();
    }

    static void example_5() throws Exception {
        TransactionManager tm = getTransactionManager();
        tm.setTransactionTimeout(3);
        tm.begin();
        Thread.sleep(1000 * 5);
        tm.commit();
    }

    static void example_4() throws Exception {
        TransactionManager tm = getTransactionManager();
        tm.begin();
        System.out.println(Status.STATUS_ACTIVE == tm.getStatus());
        tm.commit();
        System.out.println(Status.STATUS_NO_TRANSACTION == tm.getStatus());
    }

    static void example_3() throws Exception {
        TransactionManager tm = getTransactionManager();
        tm.begin();
        tm.setRollbackOnly();
        tm.commit();
    }

    static void example_2() throws Exception {

        TransactionManager tm = getTransactionManager();
        tm.begin();
        tm.rollback();
    }

    static void example_1() throws Exception {
        TransactionManager tm = getTransactionManager();
        tm.begin();
        tm.commit();
    }

}
