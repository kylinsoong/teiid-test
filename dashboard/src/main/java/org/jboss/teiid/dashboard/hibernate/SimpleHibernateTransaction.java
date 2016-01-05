package org.jboss.teiid.dashboard.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SimpleHibernateTransaction {
    
    Logger log = Logger.getLogger(SimpleHibernateTransaction.class.getName());
    
    private static transient ThreadLocal<SimpleHibernateTransaction> activeTx = new ThreadLocal<SimpleHibernateTransaction>();
    
    public static SimpleHibernateTransaction getCurrentTx() {
        SimpleHibernateTransaction tx = activeTx.get();
        if (tx == null) {
            activeTx.set(tx = new SimpleHibernateTransaction());
        }
        return tx;
    }
    
    /**
     * The transaction identifier.
     */
    private String id;

    /**
     * The current tx fragment being processed by the transaction.
     */
    SimpleHibernateTxFragment currentFragment;

    /**
     * Children listeners that were opened within this fragment interested in transaction callbacks.
     */
    List<SimpleHibernateTxFragment> listeners;

    /**
     * List of "marked new" transactions, that will be executed AFTER this tx is commited or rolled back.
     */
    List<SimpleHibernateTxFragment> followers;

    /**
     * Hibernate session associated with the transaction.
     */
    private Session session;

    /**
     * Flag indicating i the transaction has been initiated.
     */
    private boolean active;

    /**
     * Flag indicating if the transaction has been marked as rollback only.
     */
    private boolean rollback;

    /**
     * Status flag indicating if the transaction is has started the completion phase..
     */
    private boolean completing;

    /**
     * The underlying Hibernate transaction
     */
    protected Transaction tx;
    
    private SessionFactory sessionFactory;
    
    private SimpleHibernateTransaction(){
        this.id = Thread.currentThread().getName();
        this.currentFragment = null;
        this.followers = new ArrayList<SimpleHibernateTxFragment>();
        this.listeners = new ArrayList<SimpleHibernateTxFragment>();
        this.session = null;
        this.tx = null;
        this.active = false;
        this.rollback = false;
        this.completing = false;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public String getId() {
        return id;
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() {
        return session;
    }

    public void begin() throws Exception {
        try {
            log.log(Level.FINER, "Begin transaction. Id=" + getId());
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            active = true;
        } catch (HibernateException e) {
            error(e);
            throw e;
        }
    }
    
    public void complete() {
        // Flush the session before notifying the listeners
        if (!rollback) {
            flush();
        }

        // Invoke listeners
        completing = true;
        notifyListeners(true);

        // Commit or rollback
        if (rollback) {
            rollback();
        } else {
            commit();
        }

        // Close the transaction and unbound it from the current thread
        completing = false;
        active = false;
        close();
        tx = null;
        activeTx.set(null);

        // Invoke listeners
        notifyListeners(false);

        // Process queued txs.
        processFollowers();
    }
    
    /**
     * Call the followers (new transactions opened within this one, with the "new tx" flag set).
     */
    private void processFollowers() {
        for (SimpleHibernateTxFragment fragment : followers) {
            try {
                log.log(Level.FINER, "Follower");
                fragment.execute();
            } catch (Throwable e) {
                log.log(Level.SEVERE, "Follower error. Id=" + getId(), e);
            }
        }
    }
    
    /** Rollback the transaction */
    protected void rollback() {
        try {
            log.log(Level.FINER, "Rollback transaction. Id=" + getId());
            tx.rollback();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error in rollback. Id=" + getId());
            rollback = false;
            error(e);
        }
    }
    
    /** Commit the transaction */
    protected void commit() {
        try {
            log.log(Level.FINER, "Commit transaction. Id=" + getId());
            tx.commit();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error in commit. Id=" + getId());
            error(e);
        }
    }
    
    /** Flush the transaction */
    protected void close() {
        try {
            log.log(Level.FINER, "Close transaction. Id=" + getId());
            if (session.isOpen()) {
                session.close();
            }
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Close error. Id=" + getId());
            error(e);
        }
    }

    
    public void error(Throwable t) {
        if (!rollback) {
            rollback = true;
            t.printStackTrace();
        }
    }
    
    protected final void executeFragment(SimpleHibernateTxFragment fragment) throws Exception {
        FlushMode flushMode = session.getFlushMode();
        boolean flushChanged = false;
        
        try {
            // Change the current fragment.
            fragment.parentFragment = currentFragment;
            currentFragment = fragment;
            
            SimpleHibernateTxFragment flusherFragment = getFlusherFragment();
            if (fragment == flusherFragment) {
                session.setFlushMode(FlushMode.COMMIT);
                flushChanged = true;
            }
            
            fragment.txFragment(session);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private SimpleHibernateTxFragment getFlusherFragment() {
        SimpleHibernateTxFragment setter = null;
        SimpleHibernateTxFragment fragment = currentFragment;
        while (fragment != null) {
            if (fragment.flushAfterFinish) {
                setter = fragment;
            }
            fragment = fragment.parentFragment;
        }
        return setter;
    }
    
    protected void flush() {
        try {
            log.log(Level.FINER, "Flush transaction. Id=" + getId());
            session.flush();
        } catch (Throwable e) {
            log.log(Level.FINER, "Flush error. Id=" + getId());
            error(e);
        }
    }
    
    protected void notifyListeners(boolean before) {
        if (!listeners.isEmpty()) log.log(Level.FINER, (before ? "Before " : "After ") + (rollback ? "rollback" : "commit"));

        // A copy of the list is needed because a listener could modify the list if it contains a txFragment with callbacks.
        for (SimpleHibernateTxFragment listener : new ArrayList<SimpleHibernateTxFragment> (listeners)) {
            boolean wasCommit = !rollback;
            notifyListener(before, listener);

            // If the listener execution aborts the tx then the notifyBeforeRollback must be sent to all the listeners.
            if (before && wasCommit && rollback) {
                notifyListeners(true);

                // The current notifyBeforeCommit notifications must be cancelled.
                break;
            }
        }
    }
    
    protected void notifyListener(boolean before, SimpleHibernateTxFragment listener) {
        if (before) {
            if (rollback) {
                notifyBeforeRollback(listener);
            } else {
                notifyBeforeCommit(listener);
            }
        } else {
            if (rollback) {
                notifyAfterRollback(listener);
            } else {
                notifyAfterCommit(listener);
            }
        }
    }
    
    protected void notifyBeforeRollback(SimpleHibernateTxFragment fragment) {
        try {
            fragment.beforeRollback();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error before rollback: ", e);
        }
    }
    
    protected void notifyAfterRollback(SimpleHibernateTxFragment fragment) {
        try {
            fragment.afterRollback();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error after rollback: ", e);
        }
    }
    
    protected void notifyBeforeCommit(SimpleHibernateTxFragment fragment) {
        try {
            fragment.beforeCommit();
        } catch (Throwable t) {
            // If it fails then the commit must be aborted.
            error(t);
        }
    }

    protected void notifyAfterCommit(SimpleHibernateTxFragment fragment) {
        try {
            fragment.afterCommit();
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Error after commit: ", e);
        }
    }

}
