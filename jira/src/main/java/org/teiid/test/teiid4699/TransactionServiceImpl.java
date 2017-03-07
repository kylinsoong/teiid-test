package org.teiid.test.teiid4699;

import java.util.Collection;

import javax.transaction.xa.Xid;

import org.teiid.adminapi.AdminException;
import org.teiid.adminapi.impl.TransactionMetadata;
import org.teiid.client.xa.XATransactionException;
import org.teiid.client.xa.XidImpl;
import org.teiid.dqp.service.TransactionContext;
import org.teiid.dqp.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    @Override
    public void begin(TransactionContext context) throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void commit(TransactionContext context)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback(TransactionContext context)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public TransactionContext getOrCreateTransactionContext(String threadId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void suspend(TransactionContext context)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume(TransactionContext context)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public TransactionContext begin(String threadId)
            throws XATransactionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void commit(String threadId) throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback(String threadId) throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelTransactions(String threadId, boolean requestOnly)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public int prepare(String threadId, XidImpl xid, boolean singleTM)
            throws XATransactionException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void commit(String threadId, XidImpl xid, boolean onePhase,
            boolean singleTM) throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback(String threadId, XidImpl xid, boolean singleTM)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public Xid[] recover(int flag, boolean singleTM)
            throws XATransactionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void forget(String threadId, XidImpl xid, boolean singleTM)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void start(String threadId, XidImpl xid, int flags, int timeout,
            boolean singleTM) throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public void end(String threadId, XidImpl xid, int flags, boolean singleTM)
            throws XATransactionException {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<TransactionMetadata> getTransactions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void terminateTransaction(String transactionId)
            throws AdminException {
        // TODO Auto-generated method stub

    }

}
